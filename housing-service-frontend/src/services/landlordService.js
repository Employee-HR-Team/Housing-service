import api from './api';

const landlordService = {
    // Get all landlords
    getAllLandlords: () => api.get('/landlords'),
    
    // Get landlord by ID
    getLandlordById: (id) => api.get(`/landlords/${id}`),
    
    // Create new landlord
    createLandlord: (data) => api.post('/landlords', null, {
        params: {
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            phone: data.phone
        }
    }),
    
    // Update landlord details
    updateLandlord: (id, data) => api.put(`/landlords/${id}`, null, {
        params: {
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            phone: data.phone
        }
    }),
    
    // Delete landlord
    deleteLandlord: (id) => api.delete(`/landlords/${id}`),
    
    // Search landlords
    searchLandlords: (searchTerm) => api.get('/landlords/search', {
        params: { searchTerm: searchTerm }
    })
};

export default landlordService;