// src/services/reportService.js
import api from './api';

const reportService = {
  // Get all facility reports
  getAllReports: () => api.get('/facility-reports'),

  // Create a new report
  createReport: (reportData) => api.post('/facility-reports', reportData),

  // Get a specific report
  getReport: (reportId) => api.get(`/facility-reports/${reportId}`),

  // Add comment to a report
  addComment: (reportId, commentData) => 
    api.post(`/facility-reports/${reportId}/comments`, commentData),

  // Update report status (HR only)
  updateStatus: (reportId, status) => 
    api.put(`/facility-reports/${reportId}/status`, { status }),

  // Get reports by facility
  getReportsByFacility: (facilityId, page = 0, size = 10) => 
    api.get(`/facility-reports/facility/${facilityId}`, {
      params: { page, size }
    }),

  // Get reports by status
  getReportsByStatus: (status) => 
    api.get(`/facility-reports/status/${status}`),

  // Get reports by employee
  getReportsByEmployee: (employeeId) => 
    api.get(`/facility-reports/employee/${employeeId}`),

  // Get reports by house
  getReportsByHouse: (houseId) => 
    api.get(`/facility-reports/house/${houseId}`),

  // Edit a comment
  editComment: (commentId, employeeId, newComment) => 
    api.put(`/facility-reports/comments/${commentId}`, {
      employeeId,
      newComment
    }),

  // Delete a report
  deleteReport: (reportId, employeeId) => 
    api.delete(`/facility-reports/${reportId}`, {
      params: { employeeId }
    })
};

export default reportService;
