# FolksDev & Kod Gemisi SpringBoot Bootcamp
OpenApi on heroku https://http://blog-api-ebuozturk.herokuapp.com/swagger-ui.html


## Requirements
- Java 11
- Maven
- Docker

## Tech Stack

- Java 11
- Kotlin 1.3.72
- Spring Boot
- Spring Web
- Spring Data Jpa
- OpenApi
- Hamcrest
- Hateoas
- PostgreSQL
- H2 
- Flyway
- JUnit 5
- Docker
- Docker-compose

## Docker

Clone the project

```bash
  git clone https://github.com/ebuozturk/blog-app-folksdev.git
```

Go to the project directory

```bash
  cd blog-app-folksdev
```

Docker compose up

```bash
  docker-compose up
```

Application is running on 

```bash
  http://localhost:9090
```
OpenApi url 

```bash
  http://localhost:9090/swagger-ui.html
```

PostgreSQL is running on 

```bash
  http://localhost:5432
```

## API Reference
## User Controller
#### Get all users
---
```
  GET /v1/user
```
#### Get user by id
---
```
  GET /v1/user/{id}
```

#### Create user
---
```
  POST /api/items
```
#### Request body
```json
{
  "username": "string",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "email": "string",
  "birthDate": "2021-11-19"
}
```

#### Update user
---
```
  PUT /v1/user/{id}
```
#### Request body
```json
{
  "username": "string",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "email": "string",
  "birthDate": "2021-11-19"
}
```

#### Delete user
---
```
  DELETE /v1/user/{id}
```
---

## Entry Controller
#### Get all entries
---
```
  GET /v1/entry
```
#### Get entry by id
---
```
  GET /v1/entry/{id}
```
#### Get all entries by user id
---
```
  GET /v1/entry/user?id={user_id}
```

#### Create entry
---
```
  POST /api/entry
```
#### Request body
```json
{
  {
  "title": "string",
  "content": "string",
  "userId": "string"
}
}
```

#### Update entry
---
```
  PUT /v1/entry/{id}
```
#### Request body
```json
{
  "title": "string",
  "content": "string"
}
```

#### Delete entry
---
```
  DELETE /v1/entry/{id}
```
## Comment Controller

#### Get comment by comment id
---
```
  GET /v1/comment/{id}
```
#### Get all comments by user id
---
```
  GET /v1/comment/user?id={user_id}
```
#### Get all comments by entry id
---
```
  GET /v1/comment/entry?id={entry_id}
```
#### Get all comments by user id and entry id
---
```
  GET /v1/comment?entryId={entry_id}&userId={user_id}
```

#### Create comment
---
```
  POST /api/comment
```
#### Request body
```json
{
  "comment": "string",
  "userId": "string",
  "entryId": "string"
}
```

#### Update comment
---
```
  PUT /v1/comment/{id}
```
#### Request body
```json
{
  "comment": "string"
}
```

#### Delete comment
---
```
  DELETE /v1/comment/{id}
```




<h2>Unit Tests</h2>
<img src="https://user-images.githubusercontent.com/62665901/142010766-276bbd6d-edc1-43b6-9ae1-0b20fca4a85f.PNG">
<h2>Integration Tests</h2>
<img src="https://user-images.githubusercontent.com/62665901/142010763-42b5b973-d075-4895-ae12-f928fd9d10ed.PNG">

