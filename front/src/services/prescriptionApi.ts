// Drugs API endpoint (proxied to validation-backend-soap)
const DRUGS_API_URL = '/api/drugs';

import type {
  Prescription,
  CreatePrescriptionRequest,
  UpdateStatusRequest,
} from '../types/prescription';

// const API_BASE_URL = '/api/prescriptions';
const API_BASE_URL = import.meta.env.VITE_API_URL || '/api/prescriptions';

// Helper to build endpoint URLs
function getUrl(path: string = ""): string {
  if (API_BASE_URL.endsWith("/")) {
    return API_BASE_URL + path.replace(/^\//, "");
  } else {
    return API_BASE_URL + (path ? "/" + path.replace(/^\//, "") : "");
  }
}

export const prescriptionApi = {
    async getDrugs(search = '', limit = 50): Promise<any[]> {
      const params = new URLSearchParams();
      if (search) params.append('search', search);
      params.append('limit', String(limit));
      const response = await fetch(`/api/drugsLimit?${params.toString()}`);
      if (!response.ok) {
        throw new Error('Failed to fetch drugs');
      }
      return response.json();
    },
  async getAll(): Promise<Prescription[]> {
    const response = await fetch(getUrl());
    if (!response.ok) {
      throw new Error('Failed to fetch prescriptions');
    }
    return response.json();
  },

  async getById(id: string): Promise<Prescription> {
    const response = await fetch(getUrl(id));
    if (!response.ok) {
      throw new Error('Failed to fetch prescription');
    }
    return response.json();
  },

  async create(data: CreatePrescriptionRequest): Promise<Prescription> {
    const response = await fetch(getUrl(), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error('Failed to create prescription');
    }
    return response.json();
  },

  async updateStatus(id: string, data: UpdateStatusRequest): Promise<Prescription> {
    const response = await fetch(getUrl(`${id}/status`), {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error('Failed to update prescription status');
    }
    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(getUrl(id), {
      method: 'DELETE',
    });
    if (!response.ok) {
      throw new Error('Failed to delete prescription');
    }
  },
};
