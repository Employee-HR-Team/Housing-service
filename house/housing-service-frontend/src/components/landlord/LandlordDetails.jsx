import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import landlordService from '../../services/landlordService';

const LandlordDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [landlord, setLandlord] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchLandlordDetails();
  }, [id]);

  const fetchLandlordDetails = async () => {
    try {
      setLoading(true);
      const response = await landlordService.getLandlordById(id);
      setLandlord(response.data);
    } catch (err) {
      setError(err.message || 'Failed to fetch landlord details');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg">Loading landlord details...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-red-500 p-4 border border-red-300 rounded bg-red-50">
        Error: {error}
      </div>
    );
  }

  if (!landlord) return null;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-lg shadow-md">
          <div className="p-6">
            <div className="flex justify-between items-start mb-6">
              <h1 className="text-2xl font-bold">
                {landlord.firstName} {landlord.lastName}
              </h1>
              <div className="space-x-4">
                <Link
                  to={`/landlords/${id}/edit`}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  Edit
                </Link>
                <button
                  onClick={() => navigate(-1)}
                  className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                >
                  Back
                </button>
              </div>
            </div>

            {/* Contact Information */}
            <div className="mb-8">
              <h2 className="text-xl font-semibold mb-4">Contact Information</h2>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-gray-600">Email</p>
                  <p className="font-medium">{landlord.email}</p>
                </div>
                <div>
                  <p className="text-gray-600">Phone</p>
                  <p className="font-medium">{landlord.cellPhone}</p>
                </div>
              </div>
            </div>

            {/* Statistics */}
            <div className="mb-8">
              <h2 className="text-xl font-semibold mb-4">Overview</h2>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div className="bg-blue-50 p-4 rounded">
                  <p className="text-sm text-gray-600">Total Houses</p>
                  <p className="text-2xl font-bold text-blue-600">
                    {landlord.statistics.totalHouses}
                  </p>
                </div>
                <div className="bg-green-50 p-4 rounded">
                  <p className="text-sm text-gray-600">Total Residents</p>
                  <p className="text-2xl font-bold text-green-600">
                    {landlord.statistics.totalResidents}
                  </p>
                </div>
                <div className="bg-purple-50 p-4 rounded">
                  <p className="text-sm text-gray-600">Total Facilities</p>
                  <p className="text-2xl font-bold text-purple-600">
                    {landlord.statistics.totalFacilities}
                  </p>
                </div>
                <div className="bg-red-50 p-4 rounded">
                  <p className="text-sm text-gray-600">Open Reports</p>
                  <p className="text-2xl font-bold text-red-600">
                    {landlord.statistics.totalOpenReports}
                  </p>
                </div>
              </div>
            </div>

            {/* Houses */}
            <div>
              <h2 className="text-xl font-semibold mb-4">Houses</h2>
              <div className="grid gap-4">
                {landlord.houses.map((house) => (
                  <div key={house.id} className="border rounded-lg p-4">
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-medium">{house.address}</h3>
                        <p className="text-sm text-gray-600">
                          Residents: {house.currentOccupants} / {house.maxOccupant}
                        </p>
                      </div>
                      <div className="text-right">
                        <p className="text-sm text-gray-600">
                          Open Reports: {house.openFacilityReports}
                        </p>
                        <Link
                          to={`/houses/${house.id}`}
                          className="text-blue-600 hover:text-blue-800 text-sm"
                        >
                          View Details →
                        </Link>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandlordDetails;
