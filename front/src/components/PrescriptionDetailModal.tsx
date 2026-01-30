import { Prescription } from '../types/prescription';
import { X } from 'lucide-react';

interface PrescriptionDetailModalProps {
  prescription: Prescription | null;
  onClose: () => void;
}

export default function PrescriptionDetailModal({
  prescription,
  onClose,
}: PrescriptionDetailModalProps) {
  if (!prescription) return null;

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 p-4" style={{ background: 'rgba(10,24,34,0.95)' }}>
      <div className="rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto" style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)' }}>
        <div className="sticky top-0 px-6 py-4 flex justify-between items-center rounded-t-lg" style={{ background: 'var(--color-green-accent)', color: 'var(--color-bg-dark)' }}>
          <h2 className="text-2xl font-bold">Prescription Details</h2>
          <button
            onClick={onClose}
            style={{ color: 'var(--color-bg-dark)' }}
            className="hover:opacity-80 transition-colors"
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        <div className="p-6 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>
                Prescription ID
              </label>
              <p className="font-mono text-sm px-3 py-2 rounded" style={{ background: '#112233', color: 'var(--color-green-accent)' }}>
                {prescription.id}
              </p>
            </div>

            {/* Status removed */}

            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>
                Patient Name
              </label>
              <p style={{ color: 'var(--color-white)' }}>{prescription.patientName}</p>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>
                Doctor Name
              </label>
              <p style={{ color: 'var(--color-white)' }}>{prescription.doctorName}</p>
            </div>


            <div className="col-span-1 md:col-span-2">
              <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Prescription Items</label>
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y" style={{ borderColor: 'var(--color-green-accent)' }}>
                  <thead style={{ background: '#112233' }}>
                    <tr>
                      <th className="px-4 py-2 text-left text-xs font-medium uppercase" style={{ color: 'var(--color-green-accent)' }}>Drug Code</th>
                      <th className="px-4 py-2 text-left text-xs font-medium uppercase" style={{ color: 'var(--color-green-accent)' }}>Dose</th>
                      <th className="px-4 py-2 text-left text-xs font-medium uppercase" style={{ color: 'var(--color-green-accent)' }}>Frequency</th>
                      <th className="px-4 py-2 text-left text-xs font-medium uppercase" style={{ color: 'var(--color-green-accent)' }}>Duration (days)</th>
                      <th className="px-4 py-2 text-left text-xs font-medium uppercase" style={{ color: 'var(--color-green-accent)' }}>Quantity</th>
                    </tr>
                  </thead>
                  <tbody style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)' }}>
                    {prescription.items && prescription.items.length > 0 ? (
                      prescription.items.map(item => (
                        <tr key={item.id}>
                          <td className="px-4 py-2 whitespace-nowrap">{item.drugCode}</td>
                          <td className="px-4 py-2 whitespace-nowrap">{item.dose}</td>
                          <td className="px-4 py-2 whitespace-nowrap">{item.frequency}</td>
                          <td className="px-4 py-2 whitespace-nowrap">{item.durationDays}</td>
                          <td className="px-4 py-2 whitespace-nowrap">{item.quantity}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan={5} className="px-4 py-2 text-center" style={{ color: 'var(--color-green-light)' }}>No items</td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Created</label>
              <p className="text-sm" style={{ color: 'var(--color-green-accent)' }}>{formatDate(prescription.createdAt)}</p>
            </div>
          </div>

          {prescription.notes && (
            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Notes</label>
              <p className="bg-[#112233] px-4 py-3 rounded whitespace-pre-wrap" style={{ color: 'var(--color-green-accent)' }}>
                {prescription.notes}
              </p>
            </div>
          )}
        </div>

        <div className="px-6 py-4 rounded-b-lg flex justify-end" style={{ background: '#112233' }}>
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-lg transition-colors"
            style={{ background: 'var(--color-green-accent)', color: 'var(--color-bg-dark)' }}
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
