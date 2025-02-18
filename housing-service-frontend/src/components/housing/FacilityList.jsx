import React from 'react';
import StatusBadge from '../common/StatusBadge';

const FacilityList = ({ facilities, houseId, isHR = false, onUpdate }) => {
  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold">Facilities</h2>
        {isHR && (
          <button
            onClick={() => onUpdate(houseId)}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Update Facilities
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {facilities.map((facility) => (
          <div
            key={facility.id}
            className="border rounded-lg p-4 bg-white shadow-sm"
          >
            <div className="flex justify-between items-start mb-2">
              <h3 className="font-medium capitalize">{facility.type}</h3>
              <span className="text-gray-600">Qty: {facility.quantity}</span>
            </div>

            {facility.description && (
              <p className="text-gray-600 text-sm mb-3">
                {facility.description}
              </p>
            )}

            {facility.reportSummary && (
              <div className="border-t pt-3">
                <h4 className="text-sm font-medium mb-2">Report Status</h4>
                <div className="grid grid-cols-2 gap-2 text-sm">
                  <div className="flex items-center">
                    <StatusBadge status="open" />
                    <span className="ml-2">
                      {facility.reportSummary.openReports}
                    </span>
                  </div>
                  <div className="flex items-center">
                    <StatusBadge status="in_progress" />
                    <span className="ml-2">
                      {facility.reportSummary.inProgressReports}
                    </span>
                  </div>
                  <div className="flex items-center">
                    <StatusBadge status="closed" />
                    <span className="ml-2">
                      {facility.reportSummary.closedReports}
                    </span>
                  </div>
                  <div className="text-gray-600">
                    Total: {facility.reportSummary.totalReports}
                  </div>
                </div>
              </div>
            )}

            <div className="mt-3 text-sm text-gray-500">
              Last updated:{' '}
              {new Date(facility.lastModificationDate).toLocaleDateString()}
            </div>
          </div>
        ))}
      </div>

      {facilities.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          No facilities found for this house.
        </div>
      )}
    </div>
  );
};

export default FacilityList;
