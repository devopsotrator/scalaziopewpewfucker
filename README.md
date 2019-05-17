# SCALA ECHO SERVER
## JAVA NIO, TCP SOCKETS, RAW BUFFER ALLOCATION

Здесь devopsotrator публикует решение, условие задачи смотрите в TASK.md.
Первый этап решения -- простой echo server реализованный на языке программирования Scala. Сервер отрывает TCP сокет и ожидает входящие запросы. Поступающие обращения читаются в буфер, размер буфера 32 бита. Прочитанное в буфер сообщение отправляется обратно клиенту и соединение закрывается.

Подготовка окружения:

```shell
brew install scala
brew install sbt
brew install scalafmt
```

Запуск сервера:
```
sbt run
```

Проверка работы сервера
```shell
nc localhost 4713 << EOF
dnodnogrob
EOF
```

