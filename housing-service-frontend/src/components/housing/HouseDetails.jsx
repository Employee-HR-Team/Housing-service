import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import houseService from '../../services/houseService';

const HouseDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [house, setHouse] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    fetchHouseDetails();
  }, [id]);

  const fetchHouseDetails = async () => {
    try {
      setLoading(true);
      const response = await houseService.getHouse(id);
      setHouse(response.data);
    } catch (err) {
      setError(err.message || 'Failed to fetch house details');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg">Loading house details...</div>
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

  if (!house) return null;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">{house.address}</h1>
          <div className="space-x-4">
            <button
              onClick={() => navigate(`/houses/${id}/edit`)}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
              Edit
            </button>
            <button
              onClick={() => navigate(-1)}
              className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
            >
              Back
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Landlord Information */}
          <div className="border rounded-lg p-4">
            <h2 className="text-xl font-semibold mb-4">Landlord Information</h2>
            <div className="space-y-2">
              <p>
                <span className="font-medium">Name: </span>
                {house.landlordFirstName} {house.landlordLastName}
              </p>
              <p>
                <span className="font-medium">Phone: </span>
                {house.landlordPhone}
              </p>
              <p>
                <span className="font-medium">Email: </span>
                {house.landlordEmail}
              </p>
            </div>
          </div>

          {/* House Information */}
          <div className="border rounded-lg p-4">
            <h2 className="text-xl font-semibold mb-4">House Information</h2>
            <div className="space-y-2">
              <p>
                <span className="font-medium">Max Occupants: </span>
                {house.maxOccupant}
              </p>
              <p>
                <span className="font-medium">Current Residents: </span>
                {house.residents?.length || 0}
              </p>
            </div>
          </div>

          {/* Facilities */}
          <div className="border rounded-lg p-4">
            <h2 className="text-xl font-semibold mb-4">Facilities</h2>
            <div className="grid grid-cols-2 gap-4">
              {Object.entries(house.facilities || {}).map(([type, quantity]) => (
                <div key={type} className="flex justify-between">
                  <span className="capitalize">{type}:</span>
                  <span>{quantity}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Residents */}
          <div className="border rounded-lg p-4">
            <h2 className="text-xl font-semibold mb-4">Current Residents</h2>
            {house.residents?.length > 0 ? (
              <div className="space-y-2">
                {house.residents.map((resident, index) => (
                  <div key={index} className="border-b pb-2">
                    <p className="font-medium">{resident.preferredName}</p>
                    <p className="text-sm text-gray-600">{resident.phoneNumber}</p>
                    <p className="text-sm text-gray-600">{resident.email}</p>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-500">No residents</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default HouseDetails;
