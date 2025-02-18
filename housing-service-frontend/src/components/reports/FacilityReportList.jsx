import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import reportService from '../../services/reportService';

const FacilityReportList = () => {
  const { houseId } = useParams();
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchReports();
  }, [houseId]);

  const fetchReports = async () => {
    try {
      setLoading(true);
      const response = houseId 
        ? await reportService.getReportsByHouse(houseId)
        : await reportService.getAllReports();
      setReports(response.data);
    } catch (err) {
      setError(err.message || 'Failed to fetch reports');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status.toLowerCase()) {
      case 'open':
        return 'bg-red-100 text-red-800';
      case 'in progress':
        return 'bg-yellow-100 text-yellow-800';
      case 'closed':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg">Loading reports...</div>
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
        <h1 className="text-2xl font-bold">Facility Reports</h1>
        <Link
          to="/reports/new"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          New Report
        </Link>
      </div>

      <div className="grid gap-6">
        {reports.map((report) => (
          <div
            key={report.id}
            className="border rounded-lg shadow-sm hover:shadow-md transition-shadow bg-white"
          >
            <div className="p-4">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h2 className="text-xl font-semibold">{report.title}</h2>
                  <p className="text-gray-600">
                    Facility: {report.facility.type} - {report.facility.description}
                  </p>
                </div>
                <span className={`px-3 py-1 rounded-full text-sm ${getStatusColor(report.status)}`}>
                  {report.status}
                </span>
              </div>

              <p className="text-gray-700 mb-4">{report.description}</p>

              <div className="text-sm text-gray-500 mb-4">
                <p>Reported by: {report.employeeName}</p>
                <p>Created: {new Date(report.createDate).toLocaleString()}</p>
              </div>

              {report.comments && report.comments.length > 0 && (
                <div className="border-t pt-4">
                  <h3 className="font-medium mb-2">Latest Comment:</h3>
                  <div className="text-gray-700">
                    {report.comments[0].comment}
                    <div className="text-sm text-gray-500 mt-1">
                      By {report.comments[0].employeeName} on{' '}
                      {new Date(report.comments[0].createDate).toLocaleString()}
                    </div>
                  </div>
                </div>
              )}

              <div className="mt-4 flex justify-end">
                <Link
                  to={`/reports/${report.id}`}
                  className="text-blue-600 hover:text-blue-800"
                >
                  View Details →
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FacilityReportList;
