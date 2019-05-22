import java.net.InetSocketAddress

import java.nio.ByteBuffer
import java.nio.channels.{
  AsynchronousServerSocketChannel,
  AsynchronousSocketChannel
}
import java.nio.charset.Charset

import scala.concurrent._
import ExecutionContext.Implicits.global

import scodec.bits.ByteVector
import scodec.Attempt.{Failure, Successful}
import scodec.DecodeResult

import utils.Commands
import utils.Codecs

object Main extends App {
  val server = AsynchronousServerSocketChannel.open
    .bind(new InetSocketAddress("127.0.0.1", 4713))

  println("Server ready")

  while (true) {
    val client = server.accept.get

    Future {
      val clientHost = client.getRemoteAddress.toString

      println(s"Incoming connection from: $clientHost")

      val buffer = ByteBuffer.allocateDirect(256)
      var loop = true

      while (loop) {
        val bytesRead = client.read(buffer).get

        if (bytesRead < 0) {
          println(s"Read $bytesRead Bytes: STOP")
          loop = false
        } else {
          println(s"Server has read $bytesRead Bytes")

          buffer.flip

          parse(buffer, client)
        }
      }

      println(s"Server: Connection to $clientHost will be closed")
      client.close
    }
  }

  private def parse(
      byteBuffer: ByteBuffer,
      client: AsynchronousSocketChannel
  ): Unit = {
    val buffer = ByteVector.apply(byteBuffer)

    buffer.slice(0, 4) match {
      case Commands.ReqPQValue => {
        val nonce = ByteVector.low(16)
        val server_nonce = ByteVector.low(16)
        val p = 7
        val q = 7
        val server_public_key_fingerprints = 1L

        val res_pq = Codecs.ResPQCodec
          .encode(
            Commands.ResPQValue ->
              nonce ->
              server_nonce ->
              p ->
              q ->
              server_public_key_fingerprints
          )
          .getOrElse(throw new IllegalStateException("Something goes wrong"))
          .toByteBuffer

        client.write(res_pq)
      }

      case Commands.ReqDHParamsValue => {
        println(
          s"Server: Connection to ${client.getRemoteAddress.toString} will be closed"
        )

        client.close
      }

      case _ => {
        println("Unable to recognize received command")
      }
    }
  }
}
