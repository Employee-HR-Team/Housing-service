import React from 'react';
import { Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import routes from './routes';

const App = () => {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <nav className="bg-white shadow-sm">
          <div className="container mx-auto px-4">
            <div className="flex justify-between h-16">
              <div className="flex">
                <a href="/" className="flex items-center">
                  <span className="text-xl font-bold text-gray-800">
                    Housing Portal
                  </span>
                </a>
              </div>

              <div className="flex items-center space-x-4">
                <a
                  href="/houses"
                  className="text-gray-600 hover:text-gray-900 px-3 py-2"
                >
                  Houses
                </a>
                <a
                  href="/reports"
                  className="text-gray-600 hover:text-gray-900 px-3 py-2"
                >
                  Reports
                </a>
                <a
                  href="/landlords"
                  className="text-gray-600 hover:text-gray-900 px-3 py-2"
                >
                  Landlords
                </a>
              </div>
            </div>
          </div>
        </nav>

        <main className="container mx-auto px-4 py-8">
          <Routes>
            {routes.map((route) => (
              <Route
                key={route.path}
                path={route.path}
                element={
                  route.protected ? (
                    <ProtectedRoute>
                      <route.component />
                    </ProtectedRoute>
                  ) : (
                    <route.component />
                  )
                }
              />
            ))}
            <Route path="*" element={<Navigate to="/houses" replace />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  // Add your authentication logic here
  const isAuthenticated = true; // Replace with actual auth check

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default App;
