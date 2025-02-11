# Microservice-Landschaften sicher gestalten: JWT als Schlüsseltechnologie

Dieses Repo dient als Beispiel zur Implementierung von JWTs im Kontext von Microservices
und wird begleitet von einem Blogbeitrag
auf [falbers.de/blog](https://falbers.de/blog/securing-microservices-using-jwt).

Zweck ist die Erzeugung von asymmetrisch signierten JWTs durch einen Auth-Service sowie
eine Zugriffssteuerung mit ausgestellten JWTs durch weitere Services.
Ferner wird demonstriert, wie die Inter-Service-Kommunikation mit JWTs autorisiert wird.

## Inhalt

| Name               | Beschreibung                                                             |
|--------------------|--------------------------------------------------------------------------|
| Shared JWT Modul   | Dependency zur Verarbeitung und Verwendung von JWTs durch Microservices. |
| Auth-Service       | Service zur API und Ausstellung von signierten JWTs.                     | 
| Data-Service       | Mit JWT abgesicherte API. Beispiel Inter-Service-Kommunikation mit JWT.  | 
| Postman Collection | Request-Beispiele zur Interaktion mit abgesicherten Services.            | 

### Shared JWT Modul

- Konfiguration für **JwtDecoder** und **AuthenticationConverter** zur Validierung und Auswertung von JWTs
- **Client** zum Anfragen eines JWT durch einen Service
- **Interceptor** zum Anfügen von Tokens an HTTP-Requests

### Auth-Service

- Generator zum Erzeugen von JWTs mit asymmetrischer Signatur
- API zum Ausstellen von JWTs anhand von In-Memory Usern
- API mit erforderlicher Autorisierung als Data-Service

### Data-Service 

- API mit erforderlicher Autorisierung als User und Admin
- Client mit JWT-Konfiguration für abgesicherte API

### Postman Collection

- Anfragen von JWT als User
- Anfragen von JWT als Admin
- Anfragen von User-Daten
- Anfragen von Admin-Daten