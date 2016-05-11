The server requires Java SE 8 to run, maven to build. May be checked with curl.


Start server:
java -jar bin\smallbackupservice-0.0.1-SNAPSHOT.jar --backups.dir=d:\ --todo.server.url=http://127.0.0.1:9000

Parameters:
backups.dir - dir where backups will be saved
todo.server.url - todo server url


Check server:

Show list of backups command:
curl -X GET http://127.0.0.1:8080/backups

Export backuped data by backup id:
curl -X POST http://127.0.0.1:8080/exports/<backupId>

Initiate backup command:
curl -X POST http://127.0.0.1:8080/backups


Create jar from sources:
mvn clean package