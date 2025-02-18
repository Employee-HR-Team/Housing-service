import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import houseService from '../../services/houseService';

const HouseList = () => {
    const [houses, setHouses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchHouses();
    }, []);

    const fetchHouses = async () => {
        try {
            setLoading(true);
            console.log('Fetching houses...'); // Debug log
            const response = await houseService.getAllHouses();
            console.log('Response:', response); // Debug log
            setHouses(response.data);
        } catch (err) {
            console.error('Error details:', err); // Debug log
            setError(err.message || 'Failed to fetch houses');
        } finally {
            setLoading(false);
        }
    };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg">Loading houses...</div>
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

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Housing List</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {houses.map((house) => (
          <div 
            key={house.id} 
            className="border rounded-lg shadow-sm hover:shadow-md transition-shadow"
          >
            <div className="p-4">
              <h2 className="text-xl font-semibold mb-2">{house.address}</h2>
              
              <div className="space-y-2">
                <div className="flex justify-between text-gray-600">
                  <span>Occupancy:</span>
                  <span>{house.residents?.length || 0} / {house.maxOccupant}</span>
                </div>

                <div className="text-gray-600">
                  <h3 className="font-medium">Landlord:</h3>
                  <p>{house.landlordFirstName} {house.landlordLastName}</p>
                  <p className="text-sm">{house.landlordPhone}</p>
                </div>

                {house.facilities && Object.keys(house.facilities).length > 0 && (
                  <div className="mt-4">
                    <h3 className="font-medium mb-2">Facilities:</h3>
                    <div className="grid grid-cols-2 gap-2">
                      {Object.entries(house.facilities).map(([type, quantity]) => (
                        <div key={type} className="text-sm text-gray-600">
                          {type}: {quantity}
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>

              <div className="mt-4 flex justify-end space-x-4">
                <Link
                  to={`/houses/${house.id}`}
                  className="text-blue-600 hover:text-blue-800"
                >
                  View Details
                </Link>
                {house.facilityReports?.length > 0 && (
                  <Link
                    to={`/houses/${house.id}/reports`}
                    className="text-green-600 hover:text-green-800"
                  >
                    View Reports
                  </Link>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default HouseList;