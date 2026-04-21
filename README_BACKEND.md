# Election App Backend API

Simple HTTP server for the Election App with authentication and election management.

## Setup

1. Compile the Java files:
```bash
cd src/main/java
javac *.java
```

2. Start the server:
```bash
java Main
```

The server will start on port 8080.

## API Endpoints

### Authentication

- `POST /api/login` - User login
  - Body: `username=admin&password=admin`
  - Response: `{"sessionId":"session_123","username":"admin","role":"ADMIN"}`

- `POST /api/logout` - User logout
  - Body: `sessionId=session_123`
  - Response: `{"message":"Logged out successfully"}`

- `POST /api/validate-session` - Validate session
  - Body: `sessionId=session_123`
  - Response: `{"username":"admin","role":"ADMIN"}`

### Election Management

- `POST /api/register-candidate` - Register a candidate
  - Body: `name=John Doe`
  - Response: `{"id":1,"name":"John Doe","voteCount":0}`

- `POST /api/register-voter` - Register a voter
  - Body: `name=Jane Smith`
  - Response: `{"id":1,"name":"Jane Smith","hasVoted":false}`

- `POST /api/cast-vote` - Cast a vote
  - Body: `voterId=1&candidateId=1`
  - Response: `{"message":"Vote cast successfully","candidateName":"John Doe","voterName":"Jane Smith"}`

- `GET /api/results` - Get election results
  - Response: `{"candidates":[...],"totalCandidates":2,"totalVoters":3,"totalVotes":2}`

- `GET /api/candidates` - Get all candidates
  - Response: `{"candidates":[...]}`

- `GET /api/voters` - Get all voters
  - Response: `{"voters":[...]}`

## Demo Credentials

- **Admin**: admin / admin
- **User**: voter / voter

## Features

- Simple HTTP server with JSON responses
- Session-based authentication
- CORS headers for frontend integration
- In-memory data storage
- Basic election operations

## Testing

Use curl or Postman to test endpoints:

```bash
# Login
curl -X POST http://localhost:8080/api/login -d "username=admin&password=admin"

# Register candidate
curl -X POST http://localhost:8080/api/register-candidate -d "name=John Doe"

# Get results
curl http://localhost:8080/api/results
```
