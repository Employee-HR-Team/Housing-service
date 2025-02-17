import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import houseService from '../../services/houseService';

const HouseForm = ({ isEditing = false }) => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [formData, setFormData] = useState({
    address: '',
    maxOccupant: 0,
    landlordFirstName: '',
    landlordLastName: '',
    landlordEmail: '',
    landlordPhone: '',
    facilities: {
      bed: 0,
      mattress: 0,
      table: 0,
      chair: 0
    }
  });

  useEffect(() => {
    if (isEditing && id) {
      fetchHouseData();
    }
  }, [isEditing, id]);

  const fetchHouseData = async () => {
    try {
      setLoading(true);
      const response = await houseService.getHouse(id);
      const house = response.data;
      setFormData({
        address: house.address,
        maxOccupant: house.maxOccupant,
        landlordFirstName: house.landlordFirstName,
        landlordLastName: house.landlordLastName,
        landlordEmail: house.landlordEmail,
        landlordPhone: house.landlordPhone,
        facilities: house.facilities
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleFacilityChange = (facility, value) => {
    setFormData(prev => ({
      ...prev,
      facilities: {
        ...prev.facilities,
        [facility]: parseInt(value) || 0
      }
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      if (isEditing) {
        await houseService.updateHouse(id, formData);
      } else {
        await houseService.addHouse(formData);
      }
      navigate('/houses');
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <form onSubmit={handleSubmit} className="max-w-2xl mx-auto">
        <h2 className="text-2xl font-bold mb-6">
          {isEditing ? 'Edit House' : 'Add New House'}
        </h2>

        {error && (
          <div className="bg-red-50 text-red-500 p-4 rounded mb-4">
            {error}
          </div>
        )}

        <div className="space-y-6">
          {/* House Information */}
          <div className="border rounded-lg p-4">
            <h3 className="text-xl font-semibold mb-4">House Information</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Address</label>
                <input
                  type="text"
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">
                  Max Occupants
                </label>
                <input
                  type="number"
                  name="maxOccupant"
                  value={formData.maxOccupant}
                  onChange={handleChange}
                  className="w-full p-2 border rounded"
                  required
                  min="1"
                />
              </div>
            </div>
          </div>

          {/* Landlord Information */}
          <div className="border rounded-lg p-4">
            <h3 className="text-xl font-semibold mb-4">Landlord Information</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-1">
                  First Name
                </label>
                <input
                  type="text"
                  name="landlordFirstName"
                  value={formData.landlordFirstName}
                  onChange={handleChange}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">
                  Last Name
                </label>
                <input
                  type="text"
                  name="landlordLastName"
                  value={formData.landlordLastName}
                  onChange={handleChange}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Email</label>
                <input
                  type="email"
                  name="landlordEmail"
                  value={formData.landlordEmail}
                  onChange={handleChange}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Phone</label>
                <input
                  type="tel"
                  name="landlordPhone"
                  value={formData.landlordPhone}
                  onChange={handleChange}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
            </div>
          </div>

          {/* Facilities */}
          <div className="border rounded-lg p-4">
            <h3 className="text-xl font-semibold mb-4">Facilities</h3>
            <div className="grid grid-cols-2 gap-4">
              {Object.entries(formData.facilities).map(([facility, count]) => (
                <div key={facility}>
                  <label className="block text-sm font-medium mb-1 capitalize">
                    {facility}
                  </label>
                  <input
                    type="number"
                    value={count}
                    onChange={(e) => handleFacilityChange(facility, e.target.value)}
                    className="w-full p-2 border rounded"
                    min="0"
                  />
                </div>
              ))}
            </div>
          </div>

          <div className="flex justify-end space-x-4">
            <button
              type="button"
              onClick={() => navigate('/houses')}
              className="px-4 py-2 border rounded hover:bg-gray-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
              disabled={loading}
            >
              {loading ? 'Saving...' : isEditing ? 'Update House' : 'Add House'}
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default HouseForm;