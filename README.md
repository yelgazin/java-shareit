# Backend REST service ShareIt

## Cервис для шеринга вещей

Сервис позволяет публиковать информацию о вещах, проводить поиск и брать в аренду на определенное время. Реализовано 2 сервиса:
* **основной сервис** для работы с вещами; 
* **сервис шлюз** для валидации входящих запросов.

## Требования
* git
* jdk 11
* maven
* docker

## Установка и запуск
### Шаг 1. Скачивание проекта
```
git clone https://github.com/yelgazin/java-shareit.git
```

### Шаг 2. Сборка проекта
```
cd java-shareit
mvn install
```

### Шаг 3. Запуск проекта

```
docker-compose up
```

## Спецификация API
Cпецификация в формате Swagger доступна по следующему эндпоинту [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html);


## Технологический стэк

![java](https://img.shields.io/badge/java-%23ed8b00.svg?logo=openjdk&logoColor=white&style=flat)
![spring](https://img.shields.io/badge/spring-%236db33f.svg?logo=spring&logoColor=white&style=flat)
![postgres](https://img.shields.io/badge/postgres-%23336791.svg?logo=postgresql&logoColor=white&style=flat)
![hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=Hibernate&logoColor=white)
![postman](https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white)


