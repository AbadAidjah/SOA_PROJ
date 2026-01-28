

export interface PrescriptionItem {
  id: string;
  drugCode: string;
  dose: string;
  frequency: string;
  durationDays: number;
  quantity: number;
}

export interface Prescription {
  id: string;
  patientName: string;
  doctorName: string;
  reservationId?: string;
  createdAt: string;
  submittedAt: string;
  items: PrescriptionItem[];
  notes?: string;
}

export interface CreatePrescriptionRequest {
  patientName: string;
  doctorName: string;
  items: Omit<PrescriptionItem, 'id'>[];
  notes?: string;
}
