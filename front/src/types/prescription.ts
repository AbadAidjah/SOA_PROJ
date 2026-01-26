export type PrescriptionStatus =
  | 'PENDING_VALIDATION'
  | 'RESERVED'
  | 'COMPLETED'
  | 'CANCELLED';

export interface Prescription {
  id: string;
  patientName: string;
  doctorName: string;
  medication: string;
  dosage: string;
  frequency: string;
  duration: string;
  status: PrescriptionStatus;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreatePrescriptionRequest {
  patientName: string;
  doctorName: string;
  medication: string;
  dosage: string;
  frequency: string;
  duration: string;
  notes?: string;
}

export interface UpdateStatusRequest {
  status: PrescriptionStatus;
}
