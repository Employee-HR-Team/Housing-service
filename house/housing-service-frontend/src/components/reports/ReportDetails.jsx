import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import reportService from '../../services/reportService';

const ReportDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newComment, setNewComment] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchReport();
  }, [id]);

  const fetchReport = async () => {
    try {
      setLoading(true);
      const response = await reportService.getReport(id);
      setReport(response.data);
    } catch (err) {
      setError(err.message || 'Failed to fetch report details');
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = async (newStatus) => {
    try {
      setSubmitting(true);
      await reportService.updateStatus(id, newStatus);
      await fetchReport();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    try {
      setSubmitting(true);
      await reportService.addComment(id, {
        comment: newComment.trim(),
        employeeId: 1 // TODO: Get from auth context
      });
      setNewComment('');
      await fetchReport();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg">Loading report details...</div>
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

  if (!report) return null;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-md">
        <div className="p-6">
          <div className="flex justify-between items-start mb-6">
            <div>
              <h1 className="text-2xl font-bold mb-2">{report.title}</h1>
              <p className="text-gray-600">
                Facility: {report.facility.type} - {report.facility.description}
              </p>
            </div>
            <div className="flex space-x-4">
              <select
                value={report.status}
                onChange={(e) => handleStatusChange(e.target.value)}
                className="border rounded p-2"
                disabled={submitting}
              >
                <option value="OPEN">Open</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="CLOSED">Closed</option>
              </select>
              <button
                onClick={() => navigate(-1)}
                className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
              >
                Back
              </button>
            </div>
          </div>

          <div className="mb-6">
            <h2 className="text-lg font-semibold mb-2">Description</h2>
            <p className="text-gray-700">{report.description}</p>
          </div>

          <div className="mb-6">
            <h2 className="text-lg font-semibold mb-4">Comments</h2>
            <div className="space-y-4">
              {report.comments?.map((comment) => (
                <div key={comment.id} className="border rounded p-4">
                  <p className="text-gray-700">{comment.comment}</p>
                  <div className="text-sm text-gray-500 mt-2">
                    By {comment.employeeName} on{' '}
                    {new Date(comment.createDate).toLocaleString()}
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="mt-6">
            <h2 className="text-lg font-semibold mb-4">Add Comment</h2>
            <form onSubmit={handleAddComment}>
              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                className="w-full p-2 border rounded mb-4"
                rows="3"
                placeholder="Enter your comment..."
                required
              />
              <button
                type="submit"
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                disabled={submitting}
              >
                {submitting ? 'Adding...' : 'Add Comment'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportDetails;