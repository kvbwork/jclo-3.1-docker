## Сборка и запуск

```
mvnw clean package
docker-compose up
```

Для тестирования можно использовать данные:
  - логин: `admin`, `manager`, `user`
  - пароль `1234`

## Удаление

```
docker stop rest-auth-demo
docker rm rest-auth-demo
docker image rm rest-auth-demo:latest
```