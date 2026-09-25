# Messenger

A real-time chat application built with **Spring Boot**, **WebSocket** and **MongoDB**.
Users can register, create channels, invite others with a short invite code, and exchange messages instantly.

## Features

- User registration and login
- Create channels and join them with a 6-character invite code
- Real-time messaging over WebSocket
- Message history stored in MongoDB
- Simple web client (HTML + JavaScript)
- Docker support (multi-stage build)

## Tech Stack

- Java 21, Spring Boot
- Spring WebSocket
- Spring Data MongoDB
- Lombok, Jackson
- Maven, Docker

## Design Patterns

| Pattern   | Where                         | Purpose                                                        |
|-----------|-------------------------------|----------------------------------------------------------------|
| Observer  | `ChatObserver`, `UserSession` | Every user connected to a channel receives new messages        |
| Singleton | `ChatServer`                  | One central place that keeps channel subscribers and broadcasts |
| Factory   | `EntityFactory`               | Creates channels (with invite codes) and messages              |

## Project Structure

```
src/main/java/com/demo/messenger
├── config/        WebSocket configuration
├── controller/    REST controllers (auth, channels)
├── model/         User, Channel, Message
├── pattern/       Observer, Singleton, Factory
├── repository/    MongoDB repositories
└── socket/        WebSocket message handler
```

## REST API

| Method | Endpoint                     | Description                  |
|--------|------------------------------|------------------------------|
| POST   | `/api/auth/register`         | Register a new user          |
| POST   | `/api/auth/login`            | Log in                       |
| GET    | `/api/channels?user={name}`  | Get the user's channels      |
| GET    | `/api/channels/{id}`         | Get a channel with messages  |
| POST   | `/api/channels/create`       | Create a channel             |
| POST   | `/api/channels/join`         | Join a channel by invite code |
| DELETE | `/api/channels/{id}`         | Delete a channel             |

## WebSocket

Endpoint: `ws://localhost:8080/chat`

Join a channel:
```json
{ "type": "JOIN", "channelId": "<id>" }
```

Send a message:
```json
{ "type": "SEND", "channelId": "<id>", "sender": "alice", "content": "Hello!" }
```

## How to Run

**Requirements:** Java 21, MongoDB running on `localhost:27017`

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080 in your browser.

**With Docker:**

```bash
docker build -t messenger .
docker run -p 8080:8080 messenger
```

The MongoDB connection is set in `src/main/resources/application.properties`.
Inside a container, `localhost` points to the container itself, so change the URI to an address where MongoDB is reachable.
