# SCALA ECHO SERVER
## JAVA NIO, TCP SOCKETS, RAW BUFFER ALLOCATION

Здесь devopsotrator публикует решение, условие задачи смотрите в TASK.md.
Первый этап решения -- простой echo server реализованный на языке программирования Scala и JAVA NIO2.

##### JAVA NIO2
https://www.ibm.com/developerworks/library/j-nio2-1/index.html

Сервер открывает TCP сокет и ожидает входящие запросы.
Поступающие обращения читаются в буфер, размер буфера 32 бита. Прочитанное в буфер сообщение отправляется обратно клиенту и соединение закрывается.

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

#### https://homepages.thm.de/~hg51/Veranstaltungen/NVP/Folien/nvp-12.pdf
