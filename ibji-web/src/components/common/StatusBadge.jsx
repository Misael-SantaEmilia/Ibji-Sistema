import React from 'react';

const StatusBadge = ({ status }) => {
  const isActive = status === 'ATIVO';

  return (
    <span className={`status-badge ${isActive ? 'status-ativo' : 'status-inativo'}`}>
      {status}
    </span>
  );
};

export default StatusBadge;
