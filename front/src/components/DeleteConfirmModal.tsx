import { AlertTriangle, X } from 'lucide-react';

interface DeleteConfirmModalProps {
  isOpen: boolean;
  prescriptionInfo: { id: string; patientName: string; medication: string } | null;
  onConfirm: () => void;
  onClose: () => void;
  isLoading: boolean;
}

export default function DeleteConfirmModal({
  isOpen,
  prescriptionInfo,
  onConfirm,
  onClose,
  isLoading,
}: DeleteConfirmModalProps) {
  if (!isOpen || !prescriptionInfo) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-md w-full">
        <div className="bg-red-600 text-white px-6 py-4 flex justify-between items-center rounded-t-lg">
          <h2 className="text-xl font-bold">Confirm Deletion</h2>
          <button
            onClick={onClose}
            className="text-white hover:text-gray-200 transition-colors"
            disabled={isLoading}
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        <div className="p-6 space-y-4">
          <div className="flex items-start gap-3">
            <AlertTriangle className="h-6 w-6 text-red-600 flex-shrink-0 mt-1" />
            <div>
              <p className="text-gray-900 font-medium">
                Are you sure you want to delete this prescription?
              </p>
              <p className="text-sm text-gray-600 mt-1">This action cannot be undone.</p>
            </div>
          </div>

          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-sm font-medium text-gray-900">{prescriptionInfo.patientName}</p>
            <p className="text-xs text-gray-600">{prescriptionInfo.medication}</p>
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
              type="button"
              onClick={onConfirm}
              disabled={isLoading}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50"
            >
              {isLoading ? 'Deleting...' : 'Delete Prescription'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
