# Rococo

**Минимальные предусловия для запуска проекта**

- Спуллить контейнер mysql:8.1.0, zookeeper и kafka версии 7.3.2

```posh
$ docker pull mysql:8.1.0
$ docker pull confluentinc/cp-zookeeper:7.3.2
$ docker pull confluentinc/cp-kafka:7.3.2
```

- Обновить зависимости фронта:

```posh
$ cd rococo-client
$ npm i
```


# Запуск Rococo локально в IDE:

####  1. Запустить фронтенд, БД, zookeeper и kafka командами:

Запустив скрипт

```posh
$ bash localenv.sh
```

Или выполнив последовательно команды, для *nix:

```posh
docker run --name rococo-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:8.1.0

docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 -p 2181:2181 -d confluentinc/cp-zookeeper:7.3.2

docker run --name=kafka -e KAFKA_BROKER_ID=1 \
-e KAFKA_ZOOKEEPER_CONNECT=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}'):2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
-e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
-e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
-p 9092:9092 -d confluentinc/cp-kafka:7.3.2

cd rococo-client
npm run dev
```

Для Windows:

```posh
docker run --name rococo-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:8.1.0

docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 -p 2181:2181 -d confluentinc/cp-zookeeper:7.3.2

docker run --name=kafka -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}'):2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 -p 9092:9092 -d confluentinc/cp-kafka:7.3.2

cd rococo-client
npm run dev
```

Фронт стартанет в браузере на порту 3000: http://127.0.0.1:3000/  
Надо использовать именно 127.0.0.1, а не localhost

#### 2. Прописать run конфигурацию для всех сервисов rococo-* - Active profiles local

Для этого зайти в меню Run -> Edit Configurations -> выбрать main класс -> в поле Environment variables указать spring.profiles.active=local

#### 3. Запустить сервис rococo-auth c помощью gradle или командой Run в IDE:

- Запустить сервис auth

```posh
$ cd rococo-auth
$ gradle bootRun --args='--spring.profiles.active=local'
```

Или просто перейдя к main-классу приложения RococoAuthApplication выбрать run в IDEA (предварительно удостовериться, что
выполнен предыдущий **пункт 3**)

#### 4. Запустить в любой последовательности другие сервисы: rococo-artist, rococo-gateway, rococo-userdata, rococo-museum, rococo-painting