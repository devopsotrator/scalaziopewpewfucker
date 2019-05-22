package utils

import scodec.bits._

object Commands {
  val ReqPQValue: ByteVector = hex"60469778".toBitVector.toByteVector
  val ResPQValue: ByteVector = hex"05162463".toBitVector.toByteVector
  val ReqDHParamsValue: ByteVector = hex"d712e4be".toBitVector.toByteVector
}
