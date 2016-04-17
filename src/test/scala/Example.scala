import com.github.cmanou.otp.authenticators.TOTP

object Example {
  def main(args: Array[String]) {
    val totp = TOTP("example", "email@example.com", "12341234123412341234")

    println(totp.generateURI)
    println(totp.generatePin)
    println(totp.checkPin(totp.generatePin(System.currentTimeMillis/1000L+ 30)))
    println(totp.generateQRCode())
  }
}
