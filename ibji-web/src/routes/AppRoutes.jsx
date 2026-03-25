import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Pages
import Dashboard from '../pages/Dashboard';
import LoginPage from '../pages/LoginPage';
import MembrosPage from '../pages/MembrosPage';
import MinisteriosPage from '../pages/MinisteriosPage';
import AniversariantesPage from '../pages/AniversariantesPage';

// Components
import PrivateRoute from './PrivateRoute';
import Loading from '../components/common/Loading';

const AppRoutes = () => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return <Loading message="Carregando..." />;
  }

  return (
    <Routes>
      {/* Public Routes */}
      <Route 
        path="/login" 
        element={
          isAuthenticated ? <Navigate to="/" replace /> : <LoginPage />
        } 
      />

      {/* Protected Routes */}
      <Route
        path="/"
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      />

      <Route
        path="/membros"
        element={
          <PrivateRoute roles={['ADM']}>
            <MembrosPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/ministerios"
        element={
          <PrivateRoute roles={['ADM', 'LIDER']}>
            <MinisteriosPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/aniversariantes"
        element={
          <PrivateRoute>
            <AniversariantesPage />
          </PrivateRoute>
        }
      />

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

export default AppRoutes;