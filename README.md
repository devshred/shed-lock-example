# simple ShedLock application

This is a simple application using ShedLock to avoid race conditions.

To test just execute:
```shell script
./gradlew clean build docker
docker-compose -f dev/docker-compose.yml up
```
This will start two containers trying to change simultaneously the same resource. ShedLock takes care that only one container at a time is able to do that.