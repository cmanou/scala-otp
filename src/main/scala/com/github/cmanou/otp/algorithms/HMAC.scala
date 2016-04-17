package com.github.cmanou.otp.algorithms

import com.roundeights.hasher.Implicits._
import scala.language.postfixOps

sealed class HMAC(val name: String) {
  def encrypt(secret: Array[Byte], data: Array[Byte]): Array[Byte]= {
    name match {
      case HMAC.SHA1.name   => data.hmac(secret).sha1
      case HMAC.SHA256.name => data.hmac(secret).sha256
      case HMAC.SHA512.name => data.hmac(secret).sha512
    }
  }
}

object HMAC {
  case object SHA1 extends HMAC("SHA1")
  case object SHA256 extends HMAC("SHA256")
  case object SHA512 extends HMAC("SHA512")
}

