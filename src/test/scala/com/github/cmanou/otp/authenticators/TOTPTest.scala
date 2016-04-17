package com.github.cmanou.otp.authenticators

import com.github.cmanou.otp.algorithms.HMAC
import org.apache.commons.codec.binary.Base32
import org.scalatest.{WordSpec, BeforeAndAfter, OneInstancePerTest}


/*
  Test cases based on RFC6238
 */
class TOTPTest extends WordSpec with BeforeAndAfter with OneInstancePerTest {

  "A TOTP Authenticator" when {
    "using SHA1" should {
      val base32Secret = new Base32().encodeAsString("12345678901234567890".getBytes)
      val totp = TOTP("ISSUER", "ACCOUNT", base32Secret, 8)
      val testCases: Seq[(Long,String)] = Seq(
        (59L, "94287082"),
        (1111111109L, "07081804"),
        (1111111111L, "14050471"),
        (1234567890L, "89005924"),
        (2000000000L, "69279037"),
        (20000000000L, "65353130")
      )

      testCases.foreach( test => {
        s"pass for time ${test._1} equals ${test._2}" in {
          assert(totp.generatePin(test._1) === test._2 )
        }
      })
    }

    "using SHA256" should {
      val base32Secret = new Base32().encodeAsString("12345678901234567890123456789012".getBytes)
      val totp = TOTP("ISSUER", "ACCOUNT", base32Secret, 8, HMAC.SHA256)
      val testCases: Seq[(Long,String)] = Seq(
        (59L, "46119246"),
        (1111111109L, "68084774"),
        (1111111111L, "67062674"),
        (1234567890L, "91819424"),
        (2000000000L, "90698825"),
        (20000000000L, "77737706")
      )

      testCases.foreach( test => {
        s"pass for time ${test._1} equals ${test._2}" in {
          assert(totp.generatePin(test._1) === test._2 )
        }
      })
    }

    "using SHA512" should {
      val base32Secret = new Base32().encodeAsString("1234567890123456789012345678901234567890123456789012345678901234".getBytes)
      val totp = TOTP("ISSUER", "ACCOUNT", base32Secret, 8, HMAC.SHA512)
      val testCases: Seq[(Long,String)] = Seq(
        (59L, "90693936"),
        (1111111109L, "25091201"),
        (1111111111L, "99943326"),
        (1234567890L, "93441116"),
        (2000000000L, "38618901"),
        (20000000000L, "47863826")
      )

      testCases.foreach( test => {
        s"pass for time ${test._1} equals ${test._2}" in {
          assert(totp.generatePin(test._1) === test._2 )
        }
      })
    }
  }
}
