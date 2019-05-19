import java.io.IOException
import java.net.InetSocketAddress
import java.nio.{ByteBuffer, CharBuffer}
import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}
import java.nio.charset.Charset

import scala.concurrent._
import ExecutionContext.Implicits.global
import scodec.bits.ByteVector
import scodec.Attempt.{Failure, Successful}
import scodec.DecodeResult
import scodec.bits.ByteVector

import utils.CommandsHandler.{_}
import utils.Commands
import utils.Codecs

// source
// https://homepages.thm.de/~hg51/Veranstaltungen/NVP/Folien/nvp-12.pdf

object TcpFutureServer_Main extends App {

  val PORT = 4713
  val ADDRESS = "127.0.0.1"
  val asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()

  if (!asynchronousServerSocketChannel.isOpen()) {
    System.err.println("can to open channel"); System.exit(-1);
  }

  asynchronousServerSocketChannel.bind(
    new InetSocketAddress(ADDRESS, PORT)
  );

  println("Server ready Waiting for connections");

  while (true) {
    val asynchronousSocketChannelFuture =
      asynchronousServerSocketChannel.accept();
    val asynchronousSocketChannel = asynchronousSocketChannelFuture.get();

    Future {

      val host = asynchronousSocketChannel.getRemoteAddress().toString();

      println("Incoming connection from: " + host)

      val buffer = ByteBuffer.allocateDirect(32)

      var i: Int = 0
      var loop = true

      while (loop) {
        i = i + 1
        val fnr = asynchronousSocketChannel.read(buffer)
        val bytesRead = fnr.get()

        if (bytesRead < 0) {
          println("Read " + bytesRead + " Bytes: STOP")
          loop = false
        } else {
          println("Server has read " + bytesRead + " Bytes")
          buffer.flip();

          parse(buffer)

//          println("Server received from Client : " + bf.toString());
//
//          println("Server will send back to Client: " + bf.toString());
//          buffer.put((bf.toString() + "\n").getBytes());
//
//          val fnw = asynchronousSocketChannel.write(
//            ByteBuffer.wrap((bf.toString() + "\n").getBytes())
//          );
//
//          println("Server send back " + fnw.get() + " Bytes");
//          println("Server try again reading");
        }
      }
      println("Server: Connection to " + host + " will be closed");
      asynchronousSocketChannel.close();
    }
  }


  private def parse(byteBuffer: ByteBuffer): Unit = {

    val buffer = ByteVector.apply(byteBuffer)

    buffer.slice(0, 4) match {
      case Commands.ReqPQValue => {

        Codecs.ReqPQCodec.decode(buffer.bits) match {
          case Successful(decodeResult: DecodeResult[(ByteVector, ByteVector)]) =>

            val bufferResult = decodeResult.remainder.toByteVector
            val itemToEmit = generateRegPQ(decodeResult)

            byteBuffer.put((itemToEmit.toString()).getBytes());

            println("Server will send back to Client: " + byteBuffer.toString());

          case Failure(cause) =>
            println(cause.messageWithContext)
        }
      }
      case Commands.ReqDHParamsValue => {
        Codecs.ReqDHParams.decode(buffer.bits) match {
          case Successful(decodeResult: DecodeResult[((((((ByteVector, ByteVector), ByteVector), Int), Int), Long), String)]) =>

            val bufferResult  = decodeResult.remainder.toByteVector
            val itemToEmit = generateReqDHParams(decodeResult)

            byteBuffer.put((itemToEmit.toString()).getBytes());

          case Failure(cause) =>
            println(cause.messageWithContext)
        }
      }
      case buf if buf.size < 4 =>
        tryPull()
      case _ => {
        println("Unable to recognize received command")
      }
    }

    def tryPull(): Unit = {

    }
  }

}
