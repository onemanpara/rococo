docker run --name rococo-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -v rococo:/var/lib/mysqldata -d mysql:8.1.0

cd rococo-client
npm run dev