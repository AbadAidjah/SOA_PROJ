import { useState, useEffect, FormEvent } from 'react';
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
  // Each item has its own search and dropdown
  const [drugSearch, setDrugSearch] = useState<string[]>(['']);
  const [drugs, setDrugs] = useState<{ id: string; name: string }[][]>([[]]);
  const [loadingDrugs, setLoadingDrugs] = useState<boolean[]>([false]);
  const [drugsError, setDrugsError] = useState<(string|null)[]>([null]);


  // Fetch drugs for a specific item index
  const fetchDrugsForItem = (idx: number, search: string) => {
    setLoadingDrugs(prev => {
      const arr = [...prev];
      arr[idx] = true;
      return arr;
    });
    prescriptionApi.getDrugs(search, 50)
      .then((data) => {
        const mapped = Array.isArray(data) ? data.filter(Boolean).map((d) => ({ id: d.id || d, name: d.name || d })) : [];
        setDrugs(prev => {
          const arr = [...prev];
          arr[idx] = mapped;
          return arr;
        });
        setDrugsError(prev => {
          const arr = [...prev];
          arr[idx] = null;
          return arr;
        });
      })
      .catch(() => {
        setDrugsError(prev => {
          const arr = [...prev];
          arr[idx] = 'Failed to load drugs';
          return arr;
        });
      })
      .finally(() => {
        setLoadingDrugs(prev => {
          const arr = [...prev];
          arr[idx] = false;
          return arr;
        });
      });
  };

  // When modal opens, initialize arrays for each item
  useEffect(() => {
    if (!isOpen) return;
    setDrugSearch(items.map((_, i) => drugSearch[i] || ''));
    setDrugs(items.map((_, i) => drugs[i] || []));
    setLoadingDrugs(items.map((_, i) => loadingDrugs[i] || false));
    setDrugsError(items.map((_, i) => drugsError[i] || null));
    // Optionally, fetch for all items
    items.forEach((_, idx) => fetchDrugsForItem(idx, drugSearch[idx] || ''));
    // eslint-disable-next-line
  }, [isOpen]);

  // When items are added/removed, sync arrays
  useEffect(() => {
    setDrugSearch(prev => items.map((_, i) => prev[i] || ''));
    setDrugs(prev => items.map((_, i) => prev[i] || []));
    setLoadingDrugs(prev => items.map((_, i) => prev[i] || false));
    setDrugsError(prev => items.map((_, i) => prev[i] || null));
  }, [items.length]);


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
    setDrugs(prev => ([...prev, []]));
    setLoadingDrugs(prev => ([...prev, false]));
    setDrugsError(prev => ([...prev, null]));
  };

  const removeItem = (idx: number) => {
    setItems(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
    setDrugSearch(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
    setDrugs(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
    setLoadingDrugs(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
    setDrugsError(prev => prev.length > 1 ? prev.filter((_, i) => i !== idx) : prev);
  };



  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 p-4" style={{ background: 'rgba(10,24,34,0.95)' }}>
      <div className="rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto" style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)' }}>
        <div className="sticky top-0 px-6 py-4 flex justify-between items-center rounded-t-lg" style={{ background: 'var(--color-green-accent)', color: 'var(--color-bg-dark)' }}>
          <h2 className="text-2xl font-bold">Create New Prescription</h2>
          <button
            onClick={onClose}
            style={{ color: 'var(--color-bg-dark)' }}
            className="hover:opacity-80 transition-colors"
            disabled={isLoading}
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="patientName" className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>
                Patient Name *
              </label>
              <input
                type="text"
                id="patientName"
                name="patientName"
                value={patientName}
                onChange={e => setPatientName(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded-lg focus:ring-2"
                style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                placeholder="John Doe"
              />
            </div>

            <div>
              <label htmlFor="doctorName" className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>
                Doctor Name *
              </label>
              <input
                type="text"
                id="doctorName"
                name="doctorName"
                value={doctorName}
                onChange={e => setDoctorName(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded-lg focus:ring-2"
                style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                placeholder="Dr. Smith"
              />
            </div>
          </div>

          <div className="space-y-4">
            <label className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Drugs *</label>
            {items.map((item, idx) => (
              <div key={idx} className="grid grid-cols-1 md:grid-cols-5 gap-3 items-end border p-3 rounded-lg mb-2" style={{ background: '#112233', borderColor: 'var(--color-green-accent)' }}>
                <div>
                  <label className="block text-xs font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Drug</label>
                  <input
                    type="text"
                    placeholder="Search drug..."
                    value={drugSearch[idx] || ''}
                    onChange={e => {
                      const value = e.target.value;
                      setDrugSearch(prev => prev.map((s, i) => i === idx ? value : s));
                      fetchDrugsForItem(idx, value);
                    }}
                    className="w-full px-2 py-1 border rounded-lg mb-1"
                    style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                  />
                  {loadingDrugs[idx] && <div>Loading drugs...</div>}
                  {drugsError[idx] && <div className="text-red-500">{drugsError[idx]}</div>}
                  <select
                    value={item.drugCode}
                    onChange={e => handleItemChange(idx, 'drugCode', e.target.value)}
                    required
                    className="w-full px-2 py-1 border rounded-lg"
                    style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                  >
                    <option value="">Select</option>
                    {drugs[idx] && drugs[idx].map(drug => (
                      <option key={drug.id} value={drug.name}>{drug.name}</option>
                    ))}
                    {drugs[idx] && drugs[idx].length === 50 && <option disabled>...and more</option>}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Dose</label>
                  <input
                    type="text"
                    value={item.dose}
                    onChange={e => handleItemChange(idx, 'dose', e.target.value)}
                    required
                    className="w-full px-2 py-1 border rounded-lg"
                    style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                    placeholder="500mg"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Frequency</label>
                  <input
                    type="text"
                    value={item.frequency}
                    onChange={e => handleItemChange(idx, 'frequency', e.target.value)}
                    required
                    className="w-full px-2 py-1 border rounded-lg"
                    style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                    placeholder="2x/day"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Duration (days)</label>
                  <input
                    type="number"
                    min={1}
                    value={item.durationDays}
                    onChange={e => handleItemChange(idx, 'durationDays', Number(e.target.value))}
                    required
                    className="w-full px-2 py-1 border rounded-lg"
                    style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>Quantity</label>
                  <input
                    type="number"
                    min={1}
                    value={item.quantity}
                    onChange={e => handleItemChange(idx, 'quantity', Number(e.target.value))}
                    required
                    className="w-full px-2 py-1 border rounded-lg"
                    style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
                  />
                </div>
                <div>
                  <button type="button" onClick={() => removeItem(idx)} className="text-red-500 text-xs ml-2" disabled={items.length === 1}>Remove</button>
                </div>
              </div>
            ))}
            <button type="button" onClick={addItem} className="px-3 py-1 rounded" style={{ background: 'var(--color-green-light)', color: 'var(--color-bg-dark)' }}>+ Add Drug</button>
          </div>

          <div>
            <label htmlFor="notes" className="block text-sm font-medium mb-1" style={{ color: 'var(--color-green-light)' }}>
              Notes (Optional)
            </label>
            <textarea
              id="notes"
              name="notes"
              value={notes}
              onChange={e => setNotes(e.target.value)}
              rows={4}
              className="w-full px-3 py-2 border rounded-lg focus:ring-2"
              style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)', borderColor: 'var(--color-green-accent)' }}
              placeholder="Additional notes or instructions..."
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t" style={{ borderColor: 'var(--color-green-accent)' }}>
            <button
              type="button"
              onClick={onClose}
              disabled={isLoading}
              className="px-4 py-2 border rounded-lg transition-colors disabled:opacity-50"
              style={{ borderColor: 'var(--color-green-accent)', color: 'var(--color-green-accent)', background: 'transparent' }}
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="px-4 py-2 rounded-lg transition-colors disabled:opacity-50"
              style={{ background: 'var(--color-green-accent)', color: 'var(--color-bg-dark)' }}
            >
              {isLoading ? 'Creating...' : 'Create Prescription'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
