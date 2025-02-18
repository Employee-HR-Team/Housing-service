import { REPORT_STATUS } from './constants';

// Date formatters
export const formatDate = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

export const formatDateTime = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// Status formatters
export const formatReportStatus = (status) => {
  const statusMap = {
    [REPORT_STATUS.OPEN]: 'Open',
    [REPORT_STATUS.IN_PROGRESS]: 'In Progress',
    [REPORT_STATUS.CLOSED]: 'Closed'
  };
  return statusMap[status] || status;
};

// Phone number formatter
export const formatPhoneNumber = (phone) => {
  if (!phone) return '';
  const cleaned = ('' + phone).replace(/\D/g, '');
  const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
  if (match) {
    return '(' + match[1] + ') ' + match[2] + '-' + match[3];
  }
  return phone;
};

// Name formatters
export const formatFullName = (firstName, lastName) => {
  return `${firstName || ''} ${lastName || ''}`.trim();
};

// Address formatter
export const formatAddress = (buildingNumber, street, city, state, zip) => {
  const parts = [
    buildingNumber,
    street,
    city,
    state,
    zip
  ].filter(Boolean);
  return parts.join(', ');
};

// Facility quantity formatter
export const formatFacilityQuantity = (facilities) => {
  if (!facilities) return {};
  return Object.entries(facilities).reduce((acc, [type, quantity]) => {
    acc[type] = quantity;
    return acc;
  }, {});
};

// Error message formatter
export const formatErrorMessage = (error) => {
  if (typeof error === 'string') return error;
  if (error.response?.data?.message) return error.response.data.message;
  return 'An unexpected error occurred';
};

// Currency formatter
export const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(amount);
};

// Percentage formatter
export const formatPercentage = (value) => {
  return `${Math.round(value * 100)}%`;
};

// Time ago formatter
export const formatTimeAgo = (date) => {
  const seconds = Math.floor((new Date() - new Date(date)) / 1000);
  
  let interval = seconds / 31536000;
  if (interval > 1) return Math.floor(interval) + ' years ago';
  
  interval = seconds / 2592000;
  if (interval > 1) return Math.floor(interval) + ' months ago';
  
  interval = seconds / 86400;
  if (interval > 1) return Math.floor(interval) + ' days ago';
  
  interval = seconds / 3600;
  if (interval > 1) return Math.floor(interval) + ' hours ago';
  
  interval = seconds / 60;
  if (interval > 1) return Math.floor(interval) + ' minutes ago';
  
  return 'just now';
};