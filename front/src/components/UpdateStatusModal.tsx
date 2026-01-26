import { useState } from 'react';
import { X } from 'lucide-react';
import { Prescription, PrescriptionStatus } from '../types/prescription';

interface UpdateStatusModalProps {
  prescription: Prescription | null;
  onClose: () => void;
  onUpdate: (status: PrescriptionStatus) => void;
  isLoading: boolean;
}

const statusOptions: { value: PrescriptionStatus; label: string }[] = [
  { value: 'PENDING_VALIDATION', label: 'Pending Validation' },
  { value: 'RESERVED', label: 'Reserved' },
  { value: 'COMPLETED', label: 'Completed' },
  { value: 'CANCELLED', label: 'Cancelled' },
];

export default function UpdateStatusModal({
  prescription,
  onClose,
  onUpdate,
  isLoading,
}: UpdateStatusModalProps) {
  const [selectedStatus, setSelectedStatus] = useState<PrescriptionStatus>(
    prescription?.status || 'PENDING_VALIDATION'
  );

  if (!prescription) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onUpdate(selectedStatus);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-md w-full">
        <div className="bg-blue-600 text-white px-6 py-4 flex justify-between items-center rounded-t-lg">
          <h2 className="text-xl font-bold">Update Status</h2>
          <button
            onClick={onClose}
            className="text-white hover:text-gray-200 transition-colors"
            disabled={isLoading}
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Prescription
            </label>
            <div className="bg-gray-50 p-3 rounded-lg">
              <p className="text-sm font-medium text-gray-900">{prescription.patientName}</p>
              <p className="text-xs text-gray-600">{prescription.medication}</p>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Current Status
            </label>
            <p className="text-sm text-gray-900 bg-gray-50 px-3 py-2 rounded">
              {prescription.status.replace(/_/g, ' ')}
            </p>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              New Status *
            </label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value as PrescriptionStatus)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
            >
              {statusOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={onClose}
              disabled={isLoading}
              className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors disabled:opacity-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
            >
              {isLoading ? 'Updating...' : 'Update Status'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
