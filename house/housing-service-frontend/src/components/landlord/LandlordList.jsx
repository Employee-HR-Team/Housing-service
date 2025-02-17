import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import landlordService from '../../services/landlordService';

const LandlordList = () => {
  const [landlords, setLandlords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchLandlords();
  }, []);

  const fetchLandlords = async () => {
    try {
      setLoading(true);
      const response = await landlordService.getAllLandlords();
      setLandlords(response.data);
    } catch (err) {
      setError(err.message || 'Failed to fetch landlords');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchLandlords();
      return;
    }

    try {
      setLoading(true);
      const response = await landlordService.searchLandlords(searchTerm);
      setLandlords(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this landlord?')) {
      return;
    }

    try {
      await landlordService.deleteLandlord(id);
      setLandlords(landlords.filter(landlord => landlord.id !== id));
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg">Loading landlords...</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold">Landlords</h1>
          <Link
            to="/landlords/new"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Add New Landlord
          </Link>
        </div>

        <div className="flex gap-2 mb-4">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search landlords..."
            className="flex-1 p-2 border rounded"
          />
          <button
            onClick={handleSearch}
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
          >
            Search
          </button>
        </div>

        {error && (
          <div className="text-red-500 p-4 border border-red-300 rounded bg-red-50 mb-4">
            Error: {error}
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {landlords.map((landlord) => (
          <div
            key={landlord.id}
            className="border rounded-lg shadow-sm hover:shadow-md transition-shadow bg-white"
          >
            <div className="p-4">
              <h2 className="text-xl font-semibold mb-2">
                {landlord.firstName} {landlord.lastName}
              </h2>
              
              <div className="space-y-2 text-gray-600 mb-4">
                <p>Email: {landlord.email}</p>
                <p>Phone: {landlord.cellPhone}</p>
              </div>

              <div className="border-t pt-4">
                <h3 className="font-medium mb-2">Statistics:</h3>
                <div className="grid grid-cols-2 gap-2 text-sm">
                  <div>Houses: {landlord.statistics.totalHouses}</div>
                  <div>Residents: {landlord.statistics.totalResidents}</div>
                  <div>Facilities: {landlord.statistics.totalFacilities}</div>
                  <div>Open Reports: {landlord.statistics.totalOpenReports}</div>
                </div>
              </div>

              <div className="mt-4 flex justify-end space-x-2">
                <Link
                  to={`/landlords/${landlord.id}`}
                  className="text-blue-600 hover:text-blue-800 px-3 py-1"
                >
                  View
                </Link>
                <Link
                  to={`/landlords/${landlord.id}/edit`}
                  className="text-green-600 hover:text-green-800 px-3 py-1"
                >
                  Edit
                </Link>
                <button
                  onClick={() => handleDelete(landlord.id)}
                  className="text-red-600 hover:text-red-800 px-3 py-1"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default LandlordList;