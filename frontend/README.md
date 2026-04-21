# Election App Frontend

React + TypeScript frontend for the Election App with authentication.

## Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Start development server:
```bash
npm run dev
```

3. Build for production:
```bash
npm run build
```

## Features

- **Authentication**: Login/logout with demo credentials
- **Dashboard**: Overview of election statistics
- **Protected Routes**: Authenticated access only
- **API Integration**: Connects to Java backend

## Demo Credentials

- **Admin**: admin / admin
- **User**: voter / voter

## Tech Stack

- React 18 + TypeScript
- Vite (build tool)
- Tailwind CSS (styling)
- React Router (routing)
- Axios (API calls)
- TanStack Query (state management)
- Lucide React (icons)

## API Endpoints

- `POST /api/login` - User authentication
- `POST /api/logout` - User logout
- `POST /api/validate-session` - Session validation

## Project Structure

```
src/
  components/          # React components
    Login.tsx         # Login form
    ElectionDashboard.tsx # Main dashboard
  contexts/           # React contexts
    AuthContext.tsx  # Authentication state
  App.tsx            # Main app component
  main.tsx           # App entry point
```
