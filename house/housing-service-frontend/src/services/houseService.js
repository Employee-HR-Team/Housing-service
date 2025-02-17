import api from './api';

const houseService = {
  // Get all houses summary
  getAllHouses: () => api.get('/houses'),

  // Get specific house details
  getHouse: (id) => api.get(`/houses/${id}`),

  // Get house summary
  getHouseSummary: (id) => api.get(`/houses/${id}/summary`),

  // Get facility information for a house
  getHouseFacilities: (houseId) => api.get(`/houses/${houseId}/facilities`),

  // Update facility information (HR only)
  updateHouseFacilities: (houseId, facilities) => 
    api.put(`/houses/${houseId}/facilities`, facilities),

  // Add a new house (HR only)
  addHouse: (houseData) => api.post('/houses', houseData),

  // Update house details (HR only)
  updateHouse: (id, houseData) => api.put(`/houses/${id}`, houseData),

  // Delete a house (HR only)
  deleteHouse: (id) => api.delete(`/houses/${id}`),
};

export default houseService;