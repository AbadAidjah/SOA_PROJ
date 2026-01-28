// This file mocks the prescriptionApi for local development.
import type {
  Prescription,
  CreatePrescriptionRequest,
} from '../types/prescription';

const MOCK_DATA: Prescription[] = [
  {
    id: '1',
    patientName: 'John Doe',
    doctorName: 'Dr. Smith',
    notes: 'Take after meals.',
    createdAt: new Date().toISOString(),
    submittedAt: new Date().toISOString(),
    reservationId: 'mock-reservation-id',
    items: [
      {
        id: 'item-1',
        drugCode: 'Amoxicillin',
        dose: '500mg',
        frequency: '3 times daily',
        durationDays: 7,
        quantity: 21,
      },
    ],
  },
];

export const prescriptionApi = {
  async getAll(): Promise<Prescription[]> {
    return Promise.resolve(MOCK_DATA);
  },
  async getById(id: string): Promise<Prescription> {
    return Promise.resolve(MOCK_DATA.find(p => p.id === id)!);
  },
  async create(data: CreatePrescriptionRequest): Promise<Prescription> {
    const newPrescription: Prescription = {
      id: String(Date.now()),
      ...data,
      items: data.items.map(item => ({
        ...item,
        id: Math.random().toString(36).substring(2, 10),
      })),
      createdAt: new Date().toISOString(),
      submittedAt: new Date().toISOString(),
      reservationId: 'mock-reservation-id',
    };
    MOCK_DATA.push(newPrescription);
    return Promise.resolve(newPrescription);
  },
  // updateStatus method removed
  async delete(id: string): Promise<void> {
    const index = MOCK_DATA.findIndex(p => p.id === id);
    if (index !== -1) MOCK_DATA.splice(index, 1);
    return Promise.resolve();
  },
};
