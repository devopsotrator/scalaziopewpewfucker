# Тестовое задание для Scala

  Написать примитивный mtproto сервер (два состояния) с помощью scodec, scalaz-zio (для хранения и обработки состояния) и java NIO 2 (TCP).

  Сервер должен уметь выполнять первые 2 команды при инициализации DH-сессии: *req_pq* и *req_DH_params*.

  На *req_pq* отдает *res_pq* со __случайными данными__ и ждет *req_DH_params* на который закрывает коннект.

  Валидация не нужна, используйте __random__ данные и игнорируйте правила в документации какие значения полей должны быть. Проект нужен только как демонстрация __scalaz-zio + nio2 + scodec__.

  Для сериализации TL string используйте __scodec.codecs.ascii32__. Числовые кодеки все big-endian.

  Для *req_DH_params* нужен cipher: __RSA/ECB/NoPadding__.

  Состояние сессии хранить в https://scalaz.github.io/scalaz-zio/datatypes/ref.html.

Документация по mtproto:

* https://core.telegram.org/mtproto/auth_key
* https://core.telegram.org/mtproto/description#unencrypted-message
