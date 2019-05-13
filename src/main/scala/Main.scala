import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.{ByteBuffer, CharBuffer}
import java.nio.channels.{
  AsynchronousServerSocketChannel,
  AsynchronousSocketChannel
}

import java.nio.charset.Charset;
import scala.concurrent._
import ExecutionContext.Implicits.global

// source
// https://homepages.thm.de/~hg51/Veranstaltungen/NVP/Folien/nvp-12.pdf

object TcpFutureServer_Main extends App {
  val ECHO_PORT = 4713
  val asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()
  if (!asynchronousServerSocketChannel.isOpen()) {
    System.err.println("can to open channel"); System.exit(-1);
  }
  asynchronousServerSocketChannel.bind(
    new InetSocketAddress("127.0.0.1", ECHO_PORT)
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
          val charBuffer = Charset.defaultCharset().newDecoder().decode(buffer)
          println("Server received from Client : " + charBuffer.toString());
          if (buffer.hasRemaining()) {
            buffer.compact();
          } else {
            buffer.clear();
          }
          println("Server will send back to Client: " + charBuffer.toString());
          buffer.put((charBuffer.toString() + "\n").getBytes());
          val fnw = asynchronousSocketChannel.write(
            ByteBuffer.wrap((charBuffer.toString() + "\n").getBytes())
          );
          println("Server send back " + fnw.get() + " Bytes");
          println("Server try again reading");
        }
      }
      println("Server: Connection to " + host + " will be closed");
      asynchronousSocketChannel.close();
    }
  }
}
