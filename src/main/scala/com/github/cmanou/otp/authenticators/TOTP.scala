package com.github.cmanou.otp.authenticators

import java.awt.image.RenderedImage
import java.io.{ByteArrayOutputStream, File}
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.imageio.ImageIO
import com.github.cmanou.otp.algorithms.HMAC
import com.sksamuel.scrimage.{Color, Image}
import net.glxn.qrgen.javase.QRCode
import org.apache.commons.codec.binary.Base32
import scala.math.pow

case class TOTP(issuer: String,
                account: String,
                base32Secret: String,
                digits: Int = 6,
                algorithm: HMAC = HMAC.SHA1,
                period: Long = 30) {

  def checkPin(pin: String, time: Long = System.currentTimeMillis/1000L, allowedDelta: Int = 1): Boolean = {
    val pinSequence = (-allowedDelta to allowedDelta).foldLeft(Nil: Seq[String])((a, b) => generatePin(time + b*period) +: a).reverse
    pinSequence.contains(pin)
  }

  def generatePin: String = generatePin(System.currentTimeMillis/1000L)

  def generatePin(time: Long): String = {
    val msg:    Array[Byte] = BigInt(time/period).toByteArray.reverse.padTo(8, 0.toByte).reverse
    val secret: Array[Byte] = new Base32().decode(base32Secret)
    val hash:   Array[Byte] = algorithm.encrypt(secret,msg)
    val offset: Int         = hash(hash.length - 1) & 0xf
    val binary: Long        = ((hash(offset) & 0x7f) << 24) | ((hash(offset + 1) & 0xff) << 16) | ((hash(offset + 2) & 0xff) << 8 | (hash(offset + 3) & 0xff))
    val otp:    Long        = binary % pow(10, digits).toLong

    ("0" * digits + otp.toString).takeRight(digits)
  }

  def generateURI: String = s"otpauth://totp/$issuer:$account?secret=$base32Secret&digits=$digits&algorithm=${algorithm.name}&issuer=$issuer"

  def generateQRCode(size: Int = 500) = {
    val image = Image.fromFile(QRCode.from(generateURI).withSize(size,size).file)
    "data:image/png;base64," + imgToBase64String(image.awt,"png")
  }

  private def imgToBase64String(img: RenderedImage, formatName: String) = {
    val os = new ByteArrayOutputStream()
    ImageIO.write(img, formatName, Base64.getEncoder().wrap(os))
    os.toString(StandardCharsets.ISO_8859_1.name())
  }

}
