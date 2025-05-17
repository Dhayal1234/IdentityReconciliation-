# Bitespeed Identity Resolver

This Spring Boot project implements the `/identify` endpoint to resolve user identity across purchases.

## Setup

- Java 17+
- PostgreSQL
- Run using `./mvnw spring-boot:run`

## Endpoint

### POST /identify

```json
{
  "email": "user@example.com",
  "phoneNumber": "1234567890"
}
```

Returns linked contact information.
