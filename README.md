# Prescription Management System

A modern React-based frontend for managing prescriptions with a clean blue-themed interface.

## Features

- List all prescriptions in a responsive table
- View detailed prescription information
- Create new prescriptions with a user-friendly form
- Update prescription status (PENDING_VALIDATION, RESERVED, COMPLETED, CANCELLED)
- Delete prescriptions with confirmation
- Real-time data refresh
- Error handling and loading states

## Prerequisites

- Node.js 20 or higher
- A running backend service at `/api/prescriptions`

## Development

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Build for production:

```bash
npm run build
```

## Docker Deployment

### Build the Docker image:

```bash
docker build -t prescription-management .
```

### Run the container:

```bash
docker run -p 8080:80 prescription-management
```

The application will be available at `http://localhost:8080`

### Important Notes:

- The backend API should be running on `localhost:8080` (outside Docker)
- The nginx configuration proxies `/api/*` requests to `http://host.docker.internal:8080`
- If your backend runs on a different port, update the `nginx.conf` file accordingly

## API Endpoints

The frontend connects to the following backend endpoints:

- `GET /api/prescriptions` - Get all prescriptions
- `GET /api/prescriptions/{id}` - Get prescription by ID
- `POST /api/prescriptions` - Create new prescription
- `PUT /api/prescriptions/{id}/status` - Update prescription status
- `DELETE /api/prescriptions/{id}` - Delete prescription

## Technology Stack

- React 18
- TypeScript
- Tailwind CSS
- Vite
- Lucide React (icons)
- Nginx (for Docker deployment)

## Project Structure

```
src/
├── components/          # React components
│   ├── PrescriptionList.tsx
│   ├── PrescriptionDetailModal.tsx
│   ├── CreatePrescriptionModal.tsx
│   ├── UpdateStatusModal.tsx
│   └── DeleteConfirmModal.tsx
├── services/           # API service layer
│   └── prescriptionApi.ts
├── types/             # TypeScript type definitions
│   └── prescription.ts
├── App.tsx            # Main application component
└── main.tsx           # Application entry point
```
