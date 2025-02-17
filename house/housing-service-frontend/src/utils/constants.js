export const API_BASE_URL = 'http://localhost:8080/api';

export const REPORT_STATUS = {
  OPEN: 'OPEN',
  IN_PROGRESS: 'IN_PROGRESS',
  CLOSED: 'CLOSED'
};

export const FACILITY_TYPES = {
  BED: 'bed',
  MATTRESS: 'mattress',
  TABLE: 'table',
  CHAIR: 'chair'
};

export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 10,
  DEFAULT_PAGE: 0
};

export const ERROR_MESSAGES = {
  FETCH_ERROR: 'Failed to fetch data. Please try again.',
  UPDATE_ERROR: 'Failed to update. Please try again.',
  DELETE_ERROR: 'Failed to delete. Please try again.',
  CREATE_ERROR: 'Failed to create. Please try again.',
  UNAUTHORIZED: 'You are not authorized to perform this action.',
  SESSION_EXPIRED: 'Your session has expired. Please login again.'
};

export const SUCCESS_MESSAGES = {
  UPDATE_SUCCESS: 'Successfully updated.',
  DELETE_SUCCESS: 'Successfully deleted.',
  CREATE_SUCCESS: 'Successfully created.'
};

export const ROLES = {
  HR: 'HR',
  EMPLOYEE: 'EMPLOYEE'
};

export const ROUTES = {
  HOME: '/',
  HOUSES: '/houses',
  REPORTS: '/reports',
  LANDLORDS: '/landlords',
  LOGIN: '/login'
};
