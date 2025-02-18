import axios from 'axios';

const credentials = btoa('admin:admin');

const api = axios.create({
    baseURL: 'http://localhost:8081/api',  // Added /api prefix
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Basic ${credentials}`
    },
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.log('Authentication failed');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export const houseService = {
    getAllHouses: () => api.get('/houses'),
    getHouseById: (id) => api.get(`/houses/${id}`),
    createHouse: (data) => api.post('/houses', data),
    updateHouse: (id, data) => api.put(`/houses/${id}`, data),
    deleteHouse: (id) => api.delete(`/houses/${id}`),
};

export default api;