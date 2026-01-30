import { useState, useEffect } from 'react';
import { Plus, RefreshCw, ClipboardList } from 'lucide-react';
import { Prescription, CreatePrescriptionRequest } from './types/prescription';
import { prescriptionApi } from './services/prescriptionApi';
import PrescriptionList from './components/PrescriptionList';
import PrescriptionDetailModal from './components/PrescriptionDetailModal';
import CreatePrescriptionModal from './components/CreatePrescriptionModal';
// import removed: UpdateStatusModal
import DeleteConfirmModal from './components/DeleteConfirmModal';

function App() {
  const [prescriptions, setPrescriptions] = useState<Prescription[]>([]);
  const [selectedPrescription, setSelectedPrescription] = useState<Prescription | null>(null);
  // status update state removed
  const [prescriptionToDelete, setPrescriptionToDelete] = useState<{ id: string; patientName: string; medication: string } | null>(null);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [interactionError, setInteractionError] = useState<string | null>(null);

  const fetchPrescriptions = async () => {
    setIsRefreshing(true);
    setError(null);
    try {
      const data = await prescriptionApi.getAll();
      setPrescriptions(data);
    } catch (err) {
      setError('Failed to load prescriptions. Please check your backend connection.');
      console.error('Error fetching prescriptions:', err);
    } finally {
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    fetchPrescriptions();
  }, []);

  const handleCreatePrescription = async (data: CreatePrescriptionRequest) => {
    setIsLoading(true);
    setError(null);
    setInteractionError(null);
    try {
      await prescriptionApi.create(data);
      await fetchPrescriptions();
      setIsCreateModalOpen(false); // Only closes after successful creation
    } catch (err: any) {
      // Detect drug interaction error
      if (err instanceof Error && err.message.toLowerCase().includes('interaction')) {
        setInteractionError('This prescription has drug interactions. Please review the medications.');
        // Do NOT set generic error for interaction
      } else {
        setError('Failed to create prescription. Please try again.');
      }
      console.error('Error creating prescription:', err);
    } finally {
      setIsLoading(false);
    }
  };

  // handleUpdateStatus removed

  const handleDeletePrescription = async () => {
    if (!prescriptionToDelete) return;

    setIsLoading(true);
    setError(null);
    try {
      await prescriptionApi.delete(prescriptionToDelete.id);
      await fetchPrescriptions();
      setPrescriptionToDelete(null);
    } catch (err) {
      setError('Failed to delete prescription. Please try again.');
      console.error('Error deleting prescription:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleViewDetails = (prescription: Prescription) => {
    setSelectedPrescription(prescription);
  };

  // handleUpdateStatusClick removed

  const handleDeleteClick = (id: string) => {
    const prescription = prescriptions.find(p => p.id === id);
    if (prescription) {
      setPrescriptionToDelete({
        id: prescription.id,
        patientName: prescription.patientName,
        medication: prescription.items && prescription.items.length > 0
          ? prescription.items.map(item => item.drugCode).join(', ')
          : '',
      });
    }
  };

  return (
    <div className="min-h-screen" style={{ background: 'var(--color-bg-dark)', color: 'var(--color-white)' }}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div style={{ background: 'var(--color-green-accent)' }} className="p-3 rounded-lg">
                <ClipboardList className="h-8 w-8" style={{ color: 'var(--color-bg-dark)' }} />
              </div>
              <div>
                <h1 className="text-3xl font-bold" style={{ color: 'var(--color-white)' }}>Prescription Management</h1>
                <p className="mt-1" style={{ color: 'var(--color-green-light)' }}>Manage and track all prescriptions</p>
              </div>
            </div>
            <div className="flex gap-3">
              <button
                onClick={fetchPrescriptions}
                disabled={isRefreshing}
                className="flex items-center gap-2 px-4 py-2 btn-primary border-0 rounded-lg transition-colors disabled:opacity-50"
                style={{ background: 'var(--color-green-light)', color: 'var(--color-bg-dark)' }}
              >
                <RefreshCw className={`h-5 w-5 ${isRefreshing ? 'animate-spin' : ''}`} style={{ color: 'var(--color-bg-dark)' }} />
                Refresh
              </button>
              <button
                onClick={() => setIsCreateModalOpen(true)}
                className="flex items-center gap-2 px-4 py-2 btn-primary rounded-lg transition-colors"
                style={{ background: 'var(--color-green-accent)', color: 'var(--color-bg-dark)' }}
              >
                <Plus className="h-5 w-5" style={{ color: 'var(--color-bg-dark)' }} />
                New Prescription
              </button>
            </div>
          </div>
        </div>

        {error && (
          <div className="mb-6" style={{ background: '#2a1a1a', border: '1px solid #ffb3b3', color: '#ff6b6b', padding: '1rem', borderRadius: '0.5rem' }}>
            {error}
          </div>
        )}

        <PrescriptionList
          prescriptions={prescriptions}
          onViewDetails={handleViewDetails}
          // onUpdateStatus removed
          onDelete={handleDeleteClick}
        />
      </div>

      <PrescriptionDetailModal
        prescription={selectedPrescription}
        onClose={() => setSelectedPrescription(null)}
      />

      <CreatePrescriptionModal
        isOpen={isCreateModalOpen}
        onClose={() => {
          setIsCreateModalOpen(false);
          setInteractionError(null);
          setError(null); // Clear parent error when closing modal
        }}
        onSubmit={handleCreatePrescription}
        isLoading={isLoading}
        interactionError={interactionError || error}
      />

      {/* UpdateStatusModal removed */}

      <DeleteConfirmModal
        isOpen={!!prescriptionToDelete}
        prescriptionInfo={prescriptionToDelete}
        onConfirm={handleDeletePrescription}
        onClose={() => setPrescriptionToDelete(null)}
        isLoading={isLoading}
      />
    </div>
  );
}

export default App;
