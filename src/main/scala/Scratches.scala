// // https://gist.github.com/cqfd/1901b1be0cb924898d13
// import java.nio.ByteBuffer
// import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}
// import scala.concurrent.{Future, Promise}

// object Nio {
//   def accept(server: AsynchronousServerSocketChannel): Future[AsynchronousSocketChannel] = {
//     val p = Promise[AsynchronousSocketChannel]
//     server.accept(null, new CompletionHandler[AsynchronousSocketChannel, Void] {
//       def completed(client: AsynchronousSocketChannel, attachment: Void) = p.success(client)
//       def failed(e: Throwable, attachment: Void) = p.failure(e)
//     })
//     p.future
//   }

//   def read(conn: AsynchronousSocketChannel): Future[ByteVector] = {
//     val buf = ByteBuffer.allocate(1024)
//     val p = Promise[ByteVector]
//     conn.read(buf, null, new CompletionHandler[Integer, Void] {
//       def completed(numBytes: Integer, attachment: Void): Unit = {
//         buf.flip()
//         p.success(ByteVector(buf))
//       }
//       def failed(e: Throwable, attachment: Void) = p.failure(e)
//     })
//     p.future
//   }

//   def writeOnce(bs: ByteVector, sock: AsynchronousSocketChannel): Future[Integer] = {
//     val p = Promise[Integer]
//     sock.write(bs.toByteBuffer, null, new CompletionHandler[Integer, Void] {
//       def completed(numBytesWritten: Integer, attachment: Void) = p.success(numBytesWritten)
//       def failed(e: Throwable, attachment: Void) = p.failure(e)
//     })
//     p.future
//   }

//   def write(bs: ByteVector, sock: AsynchronousSocketChannel)(implicit executor: ExecutionContext): Future[Unit] = {
//     writeOnce(bs, sock).flatMap { numBytesWritten =>
//       if (numBytesWritten == bs.length) Future.successful(())
//       else write(bs.drop(numBytesWritten), sock)
//     }
//   }
// }

// package nio2examples

// import java.nio.channels._

// import scalaz._
// import Scalaz._

// package scalaz.example.nio
// import scalaz._
// import concurrent.Promise
// import effects.IO
// import nio.sockets._
// import nio.Channels._
// import Scalaz._
// import iteratees._
// import java.nio.channels.SocketChannel
// import java.nio.channels.ByteChannel
// import java.nio.ByteBuffer
// object ExampleSocketServer {
// def run(implicit s: concurrent.Strategy) {
// val server = serverSocketChannel(8080)
// val firstConnection : IO[Promise[SocketChannel]] =
//        server.map(s => FlattenI(s(head[Promise, SocketChannel]))).map(_.run.map(_.get))
// val echo : IO[Iteratee[ByteBuffer, Promise, Unit]] = firstConnection flatMap { futureSocket =>
// val consumer = writeToChannel[Promise](futureSocket.map(_.asInstanceOf[ByteChannel]))
// val reader = futureSocket map (i => readSocketChannel(i))
//       reader.get map (e => FlattenI(e(consumer)))
//     }
// val runner : IO[Unit] = echo.map(_.run)
//     runner.unsafePerformIO
//   }
// }

// import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}
// import java.net.InetSocketAddress
// import scala.concurrent.{Future, blocking}

// class Master {
//   val server: AsynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()
//   server.bind(new InetSocketAddress("localhost", port))

//   val client: Future[AsynchronousSocketChannel] = Future { blocking { server.accept().get() } }
// }

//  val selector = Selector.open()
//  val serverChannel = ServerSocketChannel.open()
//  serverChannel.configureBlocking(false)
//  serverChannel.socket().bind(new InetSocketAddress("192.168.2.1", 5000))
//  serverChannel.register(selector, SelectionKey.OP_ACCEPT)
//  def assignWork[A](
//  	serverChannel: ServerSocketChannel,
//  	selector: Selector,
//  	work:  => Future[A]) = {

//    // work
//    //recurse
//  }
//  assignWork[Unit](serverChannel, selector, Future(()))
// }

// object SocketNio2 extends App {

//   // TODO

// }
// import java.nio.ByteBuffer
// import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel}
// import scala.concurrent.{Future, Promise}

// object Nio {
//   def accept(server: AsynchronousServerSocketChannel): Future[AsynchronousSocketChannel] = {
//     val p = Promise[AsynchronousSocketChannel]
//     server.accept(null, new CompletionHandler[AsynchronousSocketChannel, Void] {
//       def completed(client: AsynchronousSocketChannel, attachment: Void) = p.success(client)
//       def failed(e: Throwable, attachment: Void) = p.failure(e)
//     })
//     p.future
//   }

//   def read(conn: AsynchronousSocketChannel): Future[ByteVector] = {
//     val buf = ByteBuffer.allocate(1024)
//     val p = Promise[ByteVector]
//     conn.read(buf, null, new CompletionHandler[Integer, Void] {
//       def completed(numBytes: Integer, attachment: Void): Unit = {
//         buf.flip()
//         p.success(ByteVector(buf))
//       }
//       def failed(e: Throwable, attachment: Void) = p.failure(e)
//     })
//     p.future
//   }

//   def writeOnce(bs: ByteVector, sock: AsynchronousSocketChannel): Future[Integer] = {
//     val p = Promise[Integer]
//     sock.write(bs.toByteBuffer, null, new CompletionHandler[Integer, Void] {
//       def completed(numBytesWritten: Integer, attachment: Void) = p.success(numBytesWritten)
//       def failed(e: Throwable, attachment: Void) = p.failure(e)
//     })
//     p.future
//   }

//   def write(bs: ByteVector, sock: AsynchronousSocketChannel)(implicit executor: ExecutionContext): Future[Unit] = {
//     writeOnce(bs, sock).flatMap { numBytesWritten =>
//       if (numBytesWritten == bs.length) Future.successful(())
//       else write(bs.drop(numBytesWritten), sock)
//     }
//   }
