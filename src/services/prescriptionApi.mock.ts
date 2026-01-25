// This file mocks the prescriptionApi for local development.
import type {
  Prescription,
  CreatePrescriptionRequest,
  UpdateStatusRequest,
} from '../types/prescription';

const MOCK_DATA: Prescription[] = [
  {
    id: '1',
    patientName: 'John Doe',
    doctorName: 'Dr. Smith',
    medication: 'Amoxicillin',
    dosage: '500mg',
    frequency: '3 times daily',
    duration: '7 days',
    status: 'PENDING_VALIDATION',
    notes: 'Take after meals.',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
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
      status: 'PENDING_VALIDATION',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
    MOCK_DATA.push(newPrescription);
    return Promise.resolve(newPrescription);
  },
  async updateStatus(id: string, data: UpdateStatusRequest): Promise<Prescription> {
    const prescription = MOCK_DATA.find(p => p.id === id);
    if (prescription) {
      prescription.status = data.status;
      prescription.updatedAt = new Date().toISOString();
    }
    return Promise.resolve(prescription!);
  },
  async delete(id: string): Promise<void> {
    const index = MOCK_DATA.findIndex(p => p.id === id);
    if (index !== -1) MOCK_DATA.splice(index, 1);
    return Promise.resolve();
  },
};
