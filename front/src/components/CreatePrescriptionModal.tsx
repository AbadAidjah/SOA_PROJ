import { useState, useEffect, useRef, FormEvent } from 'react';
import { prescriptionApi } from '../services/prescriptionApi';
import { X } from 'lucide-react';


interface PrescriptionItem {
  drugCode: string;
  dose: string;
  frequency: string;
  durationDays: number;
  quantity: number;
}

interface CreatePrescriptionModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: any) => void;
  isLoading: boolean;
}


export default function CreatePrescriptionModal({
  isOpen,
  onClose,
  onSubmit,
  isLoading,
}: CreatePrescriptionModalProps) {
  const [patientName, setPatientName] = useState('');
  const [doctorName, setDoctorName] = useState('');
  const [notes, setNotes] = useState('');
  const [items, setItems] = useState<PrescriptionItem[]>([{
    drugCode: '', dose: '', frequency: '', durationDays: 1, quantity: 1
  }]);
  // Store all drugs in a ref to avoid re-rendering on every search
  const allDrugsRef = useRef<{ id: string; name: string }[]>([]);
  const [drugs, setDrugs] = useState<{ id: string; name: string }[]>([]);
  const [drugSearch, setDrugSearch] = useState<string[]>(['']);
  const [loadingDrugs, setLoadingDrugs] = useState(false);
  const [drugsError, setDrugsError] = useState<string|null>(null);

  useEffect(() => {
    if (!isOpen) return;
    setLoadingDrugs(true);
    prescriptionApi.getDrugs('', 50)
      .then((data) => {
        const mapped = Array.isArray(data) ? data.filter(Boolean).map((d) => ({ id: d.id || d, name: d.name || d })) : [];
        setDrugs(mapped);
      })
      .catch(() => setDrugsError('Failed to load drugs'))
      .finally(() => setLoadingDrugs(false));
  }, [isOpen]);

  useEffect(() => {
    if (!isOpen) return;
    // Fetch drugs for all search fields (could be optimized to fetch only for changed idx)
    setLoadingDrugs(true);
    const lastIdx = items.length - 1;
    const search = drugSearch[lastIdx] || '';
    prescriptionApi.getDrugs(search, 50)
      .then((data) => {
        const mapped = Array.isArray(data) ? data.filter(Boolean).map((d) => ({ id: d.id || d, name: d.name || d })) : [];
        setDrugs(mapped);
      })
      .catch(() => setDrugsError('Failed to load drugs'))
      .finally(() => setLoadingDrugs(false));
  }, [drugSearch, isOpen, items.length]);


  if (!isOpen) return null;


  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    onSubmit({
      patientName,
      doctorName,
      items,
      notes,
    });
  };

  const handleItemChange = (idx: number, field: keyof PrescriptionItem, value: string | number) => {
    setItems(prev => prev.map((item, i) => i === idx ? { ...item, [field]: value } : item));
  };

  const addItem = () => {
    setItems(prev => ([...prev, { drugCode: '', dose: '', frequency: '', durationDays: 1, quantity: 1 }]));
    setDrugSearch(prev => ([...prev, '']));
  };

  const removeItem = (idx: number) => {
    setItems(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
    setDrugSearch(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
  };



  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-blue-600 text-white px-6 py-4 flex justify-between items-center rounded-t-lg">
          <h2 className="text-2xl font-bold">Create New Prescription</h2>
          <button
            onClick={onClose}
            className="text-white hover:text-gray-200 transition-colors"
            disabled={isLoading}
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="patientName" className="block text-sm font-medium text-gray-700 mb-1">
                Patient Name *
              </label>
              <input
                type="text"
                id="patientName"
                name="patientName"
                value={patientName}
                onChange={e => setPatientName(e.target.value)}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="John Doe"
              />
            </div>

            <div>
              <label htmlFor="doctorName" className="block text-sm font-medium text-gray-700 mb-1">
                Doctor Name *
              </label>
              <input
                type="text"
                id="doctorName"
                name="doctorName"
                value={doctorName}
                onChange={e => setDoctorName(e.target.value)}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Dr. Smith"
              />
            </div>
          </div>

          <div className="space-y-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">Drugs *</label>
            {loadingDrugs && <div>Loading drugs...</div>}
            {drugsError && <div className="text-red-500">{drugsError}</div>}
            {items.map((item, idx) => (
              <div key={idx} className="grid grid-cols-1 md:grid-cols-5 gap-3 items-end border p-3 rounded-lg mb-2 bg-gray-50">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Drug</label>
                  <input
                    type="text"
                    placeholder="Search drug..."
                    value={drugSearch[idx] || ''}
                    onChange={e => {
                      const value = e.target.value;
                      setDrugSearch(prev => prev.map((s, i) => i === idx ? value : s));
                    }}
                    className="w-full px-2 py-1 border border-gray-300 rounded-lg mb-1"
                  />
                  <select
                    value={item.drugCode}
                    onChange={e => handleItemChange(idx, 'drugCode', e.target.value)}
                    required
                    className="w-full px-2 py-1 border border-gray-300 rounded-lg"
                  >
                    <option value="">Select</option>
                    {drugs.map(drug => (
                      <option key={drug.id} value={drug.name}>{drug.name}</option>
                    ))}
                    {drugs.length === 50 && <option disabled>...and more</option>}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Dose</label>
                  <input
                    type="text"
                    value={item.dose}
                    onChange={e => handleItemChange(idx, 'dose', e.target.value)}
                    required
                    className="w-full px-2 py-1 border border-gray-300 rounded-lg"
                    placeholder="500mg"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Frequency</label>
                  <input
                    type="text"
                    value={item.frequency}
                    onChange={e => handleItemChange(idx, 'frequency', e.target.value)}
                    required
                    className="w-full px-2 py-1 border border-gray-300 rounded-lg"
                    placeholder="2x/day"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Duration (days)</label>
                  <input
                    type="number"
                    min={1}
                    value={item.durationDays}
                    onChange={e => handleItemChange(idx, 'durationDays', Number(e.target.value))}
                    required
                    className="w-full px-2 py-1 border border-gray-300 rounded-lg"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Quantity</label>
                  <input
                    type="number"
                    min={1}
                    value={item.quantity}
                    onChange={e => handleItemChange(idx, 'quantity', Number(e.target.value))}
                    required
                    className="w-full px-2 py-1 border border-gray-300 rounded-lg"
                  />
                </div>
                <div>
                  <button type="button" onClick={() => removeItem(idx)} className="text-red-500 text-xs ml-2" disabled={items.length === 1}>Remove</button>
                </div>
              </div>
            ))}
            <button type="button" onClick={addItem} className="px-3 py-1 bg-blue-100 text-blue-700 rounded">+ Add Drug</button>
          </div>

          <div>
            <label htmlFor="notes" className="block text-sm font-medium text-gray-700 mb-1">
              Notes (Optional)
            </label>
            <textarea
              id="notes"
              name="notes"
              value={notes}
              onChange={e => setNotes(e.target.value)}
              rows={4}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="Additional notes or instructions..."
            />
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
              {isLoading ? 'Creating...' : 'Create Prescription'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
