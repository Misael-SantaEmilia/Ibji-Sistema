import React, { createContext, useContext, useState } from 'react';

const AppContext = createContext(null);

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useApp must be used within an AppProvider');
  }
  return context;
};

export const AppProvider = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [notifications, setNotifications] = useState([]);

  const toggleSidebar = () => {
    setSidebarOpen((prev) => !prev);
  };

  const addNotification = (notification) => {
    setNotifications((prev) => [...prev, { ...notification, id: Date.now() }]);
  };

  const removeNotification = (id) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };

  const clearNotifications = () => {
    setNotifications([]);
  };

  const value = {
    sidebarOpen,
    setSidebarOpen,
    toggleSidebar,
    notifications,
    addNotification,
    removeNotification,
    clearNotifications,
  };

  return (
    <AppContext.Provider value={value}>
      {children}
    </AppContext.Provider>
  );
};

export default AppContext;