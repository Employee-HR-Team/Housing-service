import React from 'react';

const Loading = ({ size = 'default', text = 'Loading...' }) => {
  const getSizeClasses = () => {
    switch (size) {
      case 'small':
        return 'h-4 w-4 border-2';
      case 'large':
        return 'h-12 w-12 border-4';
      default:
        return 'h-8 w-8 border-3';
    }
  };

  return (
    <div className="flex flex-col items-center justify-center p-4">
      <div
        className={`${getSizeClasses()} animate-spin rounded-full border-gray-300 border-t-blue-600`}
      />
      {text && (
        <span className="mt-2 text-gray-600 text-sm">
          {text}
        </span>
      )}
    </div>
  );
};

export default Loading;
