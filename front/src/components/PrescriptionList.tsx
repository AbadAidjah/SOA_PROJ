import { Prescription } from '../types/prescription';
import { Eye, Trash2 } from 'lucide-react';

interface PrescriptionListProps {
  prescriptions: Prescription[];
  onViewDetails: (prescription: Prescription) => void;
  onDelete: (id: string) => void;
}

export default function PrescriptionList({
  prescriptions,
  onViewDetails,
  onDelete,
}: PrescriptionListProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <div className="rounded-lg shadow-md overflow-hidden" style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)' }}>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y" style={{ borderColor: 'var(--color-green-accent)' }}>
          <thead style={{ background: '#112233' }}>
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: 'var(--color-green-accent)' }}>
                Patient
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: 'var(--color-green-accent)' }}>
                Doctor
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: 'var(--color-green-accent)' }}>
                Medication
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: 'var(--color-green-accent)' }}>
                Created
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium uppercase tracking-wider" style={{ color: 'var(--color-green-accent)' }}>
                Actions
              </th>
            </tr>
          </thead>
          <tbody style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)' }}>
            {prescriptions.length === 0 ? (
              <tr>
                <td colSpan={6} className="px-6 py-8 text-center" style={{ color: 'var(--color-green-light)' }}>
                  No prescriptions found. Create one to get started!
                </td>
              </tr>
            ) : (
              prescriptions.map((prescription) => (
                <tr key={prescription.id} className="transition-colors" style={{ borderBottom: '1px solid #112233' }}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium" style={{ color: 'var(--color-white)' }}>
                      {prescription.patientName}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm" style={{ color: 'var(--color-white)' }}>{prescription.doctorName}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {prescription.items && prescription.items.length > 0 ? (
                      <div className="text-sm" style={{ color: 'var(--color-white)' }}>
                        {prescription.items.map(item => item.drugCode).join(', ')}
                      </div>
                    ) : (
                      <div className="text-xs" style={{ color: 'var(--color-green-light)' }}>No items</div>
                    )}
                  </td>
                  {/* Status cell removed */}
                  <td className="px-6 py-4 whitespace-nowrap text-sm" style={{ color: 'var(--color-green-accent)' }}>
                    {formatDate(prescription.createdAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <div className="flex justify-end gap-2">
                      <button
                        onClick={() => onViewDetails(prescription)}
                        className="transition-colors"
                        title="View details"
                        style={{ color: 'var(--color-green-accent)' }}
                      >
                        <Eye className="h-5 w-5" />
                      </button>
                      {/* Update status button removed */}
                      <button
                        onClick={() => onDelete(prescription.id)}
                        className="transition-colors"
                        title="Delete"
                        style={{ color: '#ff6b6b' }}
                      >
                        <Trash2 className="h-5 w-5" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
