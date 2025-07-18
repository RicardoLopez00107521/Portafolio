import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import '@testing-library/jest-dom';
import Appointments from '../pages/Appointments';
import { 
  fetchDoctorJitsiInfo, 
  fetchAppointments,
  fetchPatients,
  fetchServices,
  fetchSlots 
} from '../http/api';
import { toast } from 'sonner';

// Mock de las dependencias
jest.mock('../http/api');
jest.mock('sonner');
jest.mock('../components/skeletonCard', () => ({
  SkeletonCard: () => <div data-testid="skeleton-card">Loading...</div>
}));

// Mock de los componentes UI
jest.mock('../components/ui/skeleton', () => ({
  Skeleton: () => <div data-testid="skeleton">Loading...</div>
}));

jest.mock('../components/ui/button', () => ({
  Button: ({ children, onClick }) => (
    <button onClick={onClick}>{children}</button>
  )
}));

jest.mock('../components/ui/card', () => ({
  Card: ({ children }) => <div data-testid="card">{children}</div>,
  CardHeader: ({ children }) => <div data-testid="card-header">{children}</div>,
  CardContent: ({ children }) => <div data-testid="card-content">{children}</div>,
  CardTitle: ({ children }) => <div data-testid="card-title">{children}</div>,
  CardDescription: ({ children }) => <div data-testid="card-description">{children}</div>
}));

// Mock de la API de Jitsi
const mockJitsiApi = {
  dispose: jest.fn(),
  addEventListener: jest.fn(),
};

// Mock global de la API externa de Jitsi
global.JitsiMeetExternalAPI = jest.fn(() => mockJitsiApi);

describe('Integración de Telemedicina', () => {
  let queryClient;

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false,
        },
      },
    });

    jest.clearAllMocks();

    // Mock de todas las consultas necesarias
    const mockAppointment = {
      Id: '1',
      patient: {
        Name: 'Test Patient',
        Email: 'test@example.com',
        Gender: 'M',
        BloodGroup: 'A+'
      },
      Problem: 'Test Problem',
      Modality: 'Virtual',
      Status: 'scheduled',
      Date: '2024-03-20',
      service: {
        Name: 'Telemedicina',
        Price: 100
      },
      slot: {
        Time: '10:00'
      }
    };

    fetchAppointments.mockResolvedValue([mockAppointment]);
    fetchDoctorJitsiInfo.mockResolvedValue({
      meeting_id: 'some-id',
      meeting_url: 'https://meet.jit.si/test-room',
      token: 'test-token'
    });

    fetchPatients.mockResolvedValue([
      {
        id: '1',
        Name: 'Test Patient',
        Email: 'test@example.com',
      },
    ]);

    fetchServices.mockResolvedValue([
      {
        id: '1',
        Name: 'Telemedicina',
        Price: 100,
      },
    ]);

    fetchSlots.mockResolvedValue([
      {
        id: '1',
        Time: '10:00',
        available: true,
      },
    ]);

    const mockScript = document.createElement('script');
    mockScript.id = 'jitsi-iframe-api';
    document.body.appendChild(mockScript);

    // Mock de la función window.JitsiMeetExternalAPI
    window.JitsiMeetExternalAPI = jest.fn(() => mockJitsiApi);
  });

  afterEach(() => {
    const script = document.getElementById('jitsi-iframe-api');
    if (script) {
      script.remove();
    }
    queryClient.clear();
  });

  const renderWithQueryClient = (component) => {
    return render(
      <QueryClientProvider client={queryClient}>
        {component}
      </QueryClientProvider>
    );
  };

  test('debería cargar correctamente el componente de citas con opciones de telemedicina', async () => {
    renderWithQueryClient(<Appointments />);
    
    await waitFor(() => {
      expect(screen.getByText('Test Patient')).toBeInTheDocument();
      expect(screen.getByText('Test Problem')).toBeInTheDocument();
      expect(screen.getByText('Virtual')).toBeInTheDocument();
    });

    // Hacer clic en la fila de la cita
    const patientCell = screen.getByText('Test Patient');
    fireEvent.click(patientCell.closest('tr'));

    // Verificar que se muestra el detalle de la cita
    await waitFor(() => {
      expect(screen.getByText('Patient Details')).toBeInTheDocument();
      expect(screen.getByText('Problem Details')).toBeInTheDocument();
      expect(screen.getByText('Join video call')).toBeInTheDocument();
    });
  });

  test('debería iniciar correctamente una sesión de telemedicina', async () => {
    fetchDoctorJitsiInfo.mockResolvedValue({
      meeting_id: 'some-id',
      meeting_url: 'https://meet.jit.si/test-room',
      token: 'test-token'
    });

    renderWithQueryClient(<Appointments />);

    await waitFor(() => {
      expect(screen.getByText('Test Patient')).toBeInTheDocument();
    });

    // Hacer clic en la fila de la cita
    const patientCell = screen.getByText('Test Patient');
    fireEvent.click(patientCell.closest('tr'));

    // Esperar a que aparezca el detalle y el botón
    await waitFor(() => {
      expect(screen.getByText('Problem Details')).toBeInTheDocument();
    });

    // Hacer clic en el botón de videollamada
    const joinButton = screen.getByText('Join video call');
    fireEvent.click(joinButton);

    await waitFor(() => {
      expect(fetchDoctorJitsiInfo).toHaveBeenCalled();
      expect(global.JitsiMeetExternalAPI).toHaveBeenCalledWith(
        'meet.jit.si',
        expect.objectContaining({
          roomName: 'test-room/undefined',
        })
      );
      expect(screen.getByText('Video call in progress')).toBeInTheDocument();
    });
  });

  test('debería manejar correctamente errores al iniciar la videollamada', async () => {
    fetchDoctorJitsiInfo.mockRejectedValue(new Error('Error de conexión'));

    renderWithQueryClient(<Appointments />);

    await waitFor(() => {
      expect(screen.getByText('Test Patient')).toBeInTheDocument();
    });

    // Hacer clic en la fila de la cita
    const patientCell = screen.getByText('Test Patient');
    fireEvent.click(patientCell.closest('tr'));

    // Esperar a que aparezca el detalle y el botón
    await waitFor(() => {
      expect(screen.getByText('Problem Details')).toBeInTheDocument();
    });

    // Hacer clic en el botón de videollamada
    const joinButton = screen.getByText('Join video call');
    fireEvent.click(joinButton);

    await waitFor(() => {
      expect(toast).toHaveBeenCalledWith(
        'Error connecting to video call',
        expect.any(Object)
      );
    });
  });

  test('debería finalizar correctamente una sesión de telemedicina', async () => {
    fetchDoctorJitsiInfo.mockResolvedValue({
      meeting_id: 'some-id',
      meeting_url: 'https://meet.jit.si/test-room',
      token: 'test-token'
    });

    renderWithQueryClient(<Appointments />);

    await waitFor(() => {
      expect(screen.getByText('Test Patient')).toBeInTheDocument();
    });

    // Hacer clic en la fila de la cita
    const patientCell = screen.getByText('Test Patient');
    fireEvent.click(patientCell.closest('tr'));

    // Esperar a que aparezca el detalle y el botón
    await waitFor(() => {
      expect(screen.getByText('Problem Details')).toBeInTheDocument();
    });

    // Hacer clic en el botón de videollamada
    const joinButton = screen.getByText('Join video call');
    fireEvent.click(joinButton);

    await waitFor(() => {
      expect(screen.getByText('Video call in progress')).toBeInTheDocument();
    });

    const endButton = screen.getByText('End call');
    fireEvent.click(endButton);

    await waitFor(() => {
      expect(mockJitsiApi.dispose).toHaveBeenCalled();
    });
  });
});