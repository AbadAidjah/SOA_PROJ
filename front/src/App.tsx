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
    try {
      await prescriptionApi.create(data);
      await fetchPrescriptions();
      setIsCreateModalOpen(false);
    } catch (err) {
      setError('Failed to create prescription. Please try again.');
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
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-blue-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="bg-blue-600 p-3 rounded-lg">
                <ClipboardList className="h-8 w-8 text-white" />
              </div>
              <div>
                <h1 className="text-3xl font-bold text-gray-900">Prescription Management</h1>
                <p className="text-gray-600 mt-1">Manage and track all prescriptions</p>
              </div>
            </div>
            <div className="flex gap-3">
              <button
                onClick={fetchPrescriptions}
                disabled={isRefreshing}
                className="flex items-center gap-2 px-4 py-2 bg-white text-blue-600 border border-blue-600 rounded-lg hover:bg-blue-50 transition-colors disabled:opacity-50"
              >
                <RefreshCw className={`h-5 w-5 ${isRefreshing ? 'animate-spin' : ''}`} />
                Refresh
              </button>
              <button
                onClick={() => setIsCreateModalOpen(true)}
                className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                <Plus className="h-5 w-5" />
                New Prescription
              </button>
            </div>
          </div>
        </div>

        {error && (
          <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
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
        onClose={() => setIsCreateModalOpen(false)}
        onSubmit={handleCreatePrescription}
        isLoading={isLoading}
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
