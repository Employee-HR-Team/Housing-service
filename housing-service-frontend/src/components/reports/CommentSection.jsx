import React, { useState } from 'react';
import reportService from '../../services/reportService';

const CommentSection = ({ reportId, comments = [], onCommentAdded }) => {
  const [newComment, setNewComment] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editedComment, setEditedComment] = useState('');

  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    try {
      setLoading(true);
      await reportService.addComment(reportId, {
        facilityReportId: reportId,
        employeeId: 1, // TODO: Get from auth context
        comment: newComment.trim()
      });
      setNewComment('');
      if (onCommentAdded) onCommentAdded();
    } catch (err) {
      setError(err.message || 'Failed to add comment');
    } finally {
      setLoading(false);
    }
  };

  const handleEditComment = async (commentId) => {
    if (!editedComment.trim()) return;

    try {
      setLoading(true);
      await reportService.editComment(commentId, 1, editedComment); // 1 is employeeId
      setEditingCommentId(null);
      setEditedComment('');
      if (onCommentAdded) onCommentAdded();
    } catch (err) {
      setError(err.message || 'Failed to edit comment');
    } finally {
      setLoading(false);
    }
  };

  const startEditing = (comment) => {
    setEditingCommentId(comment.id);
    setEditedComment(comment.comment);
  };

  return (
    <div className="space-y-6">
      <h2 className="text-lg font-semibold">Comments</h2>

      {error && (
        <div className="text-red-500 p-4 border border-red-300 rounded bg-red-50">
          {error}
        </div>
      )}

      {/* Comment List */}
      <div className="space-y-4">
        {comments.map((comment) => (
          <div key={comment.id} className="border rounded p-4">
            {editingCommentId === comment.id ? (
              <div className="space-y-2">
                <textarea
                  value={editedComment}
                  onChange={(e) => setEditedComment(e.target.value)}
                  className="w-full p-2 border rounded"
                  rows="3"
                />
                <div className="flex justify-end space-x-2">
                  <button
                    onClick={() => setEditingCommentId(null)}
                    className="text-gray-600 hover:text-gray-800"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={() => handleEditComment(comment.id)}
                    className="text-blue-600 hover:text-blue-800"
                    disabled={loading}
                  >
                    Save
                  </button>
                </div>
              </div>
            ) : (
              <>
                <p className="text-gray-700">{comment.comment}</p>
                <div className="flex justify-between items-center mt-2">
                  <div className="text-sm text-gray-500">
                    By {comment.employeeName} on{' '}
                    {new Date(comment.createDate).toLocaleString()}
                  </div>
                  <button
                    onClick={() => startEditing(comment)}
                    className="text-blue-600 hover:text-blue-800 text-sm"
                  >
                    Edit
                  </button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>

      {/* Add Comment Form */}
      <form onSubmit={handleAddComment} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">
            Add a comment
          </label>
          <textarea
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            className="w-full p-2 border rounded"
            rows="3"
            placeholder="Enter your comment..."
            required
          />
        </div>
        <div className="flex justify-end">
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            disabled={loading}
          >
            {loading ? 'Adding...' : 'Add Comment'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CommentSection;