
import React from 'react';

const StatusBadge = ({ status }) => {
  const getStatusStyles = (status) => {
    switch (status.toLowerCase()) {
      case 'open':
        return 'bg-red-100 text-red-800';
      case 'in_progress':
        return 'bg-yellow-100 text-yellow-800';
      case 'closed':
        return 'bg-green-100 text-green-800';
      case 'pending':
        return 'bg-blue-100 text-blue-800';
      case 'rejected':
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatStatus = (status) => {
    return status
      .toLowerCase()
      .split('_')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  };

  const statusClass = getStatusStyles(status);

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${statusClass}`}
    >
      {formatStatus(status)}
    </span>
  );
};

export default StatusBadge;