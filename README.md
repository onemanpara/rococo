# Rococo

**Rococo - путеводитель в мире живописи! Исследуйте уникальные картины, познакомьтесь с их авторами, загляните в музеи из разных уголков мира.**

**Минимальные предусловия для запуска проекта**

- На Windows рекомендуется используется терминал bash а не powershell 
- Установить Java версии 17 или новее. Это необходимо, т.к. проект не поддерживает версии <17
- Установить пакетый менеджер для сборки front-end npm [Инструкция](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).
Рекомендованная версия Node.js - 18.13.0 (LTS)
- Спуллить контейнер mysql:8.0.33, zookeeper и kafka версии 7.3.2

```posh
$ docker pull mysql:8.0.33
$ docker pull confluentinc/cp-zookeeper:7.3.2
$ docker pull confluentinc/cp-kafka:7.3.2
```

- Создать volume для сохранения данных из БД в docker на вашем компьютере

```posh
docker volume create rococo-mysql
```

# Запуск Rococo локально в IDE:

####  1. Запустить фронтенд, БД, zookeeper и kafka командами:

Запустив скрипт, для *nix:

```posh
$ bash localenv.sh
```

Запустив скрипт, для windows:

```posh
$ bash localenv-windows.sh
```

Или выполнив последовательно команды, для *nix:

```posh
docker run --name rococo-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:8.0.33

docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 -p 2181:2181 -d confluentinc/cp-zookeeper:7.3.2

docker run --name=kafka -e KAFKA_BROKER_ID=1 \
-e KAFKA_ZOOKEEPER_CONNECT=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}'):2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
-e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
-e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
-p 9092:9092 -d confluentinc/cp-kafka:7.3.2

cd rococo-client
npm i
npm run dev
```

Для Windows:

```posh
docker run --name rococo-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:8.0.33

docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 -p 2181:2181 -d confluentinc/cp-zookeeper:7.3.2

docker run --name=kafka -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}'):2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 -p 9092:9092 -d confluentinc/cp-kafka:7.3.2

cd rococo-client
npm i
npm run dev
```

Фронт стартанет в браузере на порту 3000: http://127.0.0.1:3000/  
Надо использовать именно 127.0.0.1, а не localhost

#### 2. Прописать run конфигурацию для всех сервисов rococo-* - Active profiles local

Для этого зайти в меню Run -> Edit Configurations -> выбрать main класс -> в поле Environment variables указать spring.profiles.active=local

#### 3. Запустить сервис rococo-auth c помощью gradle или командой Run в IDE:

```posh
$ cd rococo-auth
$ gradle bootRun --args='--spring.profiles.active=local'
```

Или просто перейдя к main-классу приложения RococoAuthApplication выбрать run в IDEA (предварительно удостовериться, что
выполнен предыдущий **пункт 2**)

#### 4. Запустить в любой последовательности другие сервисы: rococo-gateway, rococo-userdata, rococo-artist, rococo-geo, rococo-museum, rococo-painting

# Запуск Rococo в докере:

#### 1. Создать бесплатную учетную запись на https://hub.docker.com/ (если отсутствует)

#### 2. Создать в настройках своей учетной записи access_token

[Инструкция](https://docs.docker.com/docker-hub/access-tokens/).

#### 3. Выполнить docker login с созданным access_token (в инструкции это описано)

#### 4. Прописать в etc/hosts элиас для Docker-имени

#### frontend:  127.0.0.1 client.rococo.dc,

#### auth:      127.0.0.1 auth.rococo.dc

#### gateway:   127.0.0.1 gateway.rococo.dc

Для *nix:
```posh
$ vi /etc/hosts
```

```posh
##
# Host Database
#
# localhost is used to configure the loopback interface
# when the system is booting.  Do not change this entry.
##
127.0.0.1       localhost
127.0.0.1       client.rococo.dc
127.0.0.1       auth.rococo.dc
127.0.0.1       gateway.rococo.dc
```

В windows файл hosts лежит по пути: 
```
C:\Windows\System32\drivers\etc\hosts
```

#### 5. Перейти в корневой каталог проекта

```posh
$ cd rococo
```

#### 6. Запустить все сервисы

```posh
$ bash docker-compose-dev.sh
```

Текущая версия docker-compose-dev.sh удалит все старые Docker контейнеры в системе, поэтому если у вас есть созданные
контейнеры для других проектов - отредактируйте строку ```posh docker rm $(docker ps -a -q)```, чтобы включить в grep
только те контейнеры, которые относятся к rococo.

Rococo при запуске в докере будет работать для вас по адресу http://client.rococo.dc:80, этот порт не нужно
указывать в браузере, таким образом переходить напрямую по ссылке http://client.rococo.dc
