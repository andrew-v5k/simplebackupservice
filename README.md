The server requires Java SE 8 to run, maven to build. May be checked with curl.

### Start server:
```sh
java -jar bin\smallbackupservice-0.0.1-SNAPSHOT.jar --backups.dir=d:\ --todo.server.url=http://127.0.0.1:9000
```

Parameters:
> backups.dir - dir where backups will be saved

> todo.server.url - todo server url

### Check server:

Show list of backups command:
```sh
curl -X GET http://127.0.0.1:8080/backups
```
Export backuped data by backup id command:
```sh
curl -X POST http://127.0.0.1:8080/exports/
```
Initiate backup command:
```sh
curl -X POST http://127.0.0.1:8080/backups
```

### Create jar from sources:
```sh
mvn clean package
```
