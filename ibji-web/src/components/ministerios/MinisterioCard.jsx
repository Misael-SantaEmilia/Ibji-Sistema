import React from 'react';
import { FaEdit, FaTrash, FaUserCheck, FaUser, FaUsers } from 'react-icons/fa';
import StatusBadge from '../common/StatusBadge';

const MinisterioCard = ({ ministerio, onEdit, onInativar, onReativar }) => {
  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  };

  return (
    <div className={`ministerio-card ${ministerio.status === 'INATIVO' ? 'inactive' : ''}`}>
      <div className="ministerio-card-header">
        <div className="ministerio-card-icon">
          <FaUsers />
        </div>
        <div className="ministerio-card-info">
          <h3 className="ministerio-card-name">{ministerio.descricao}</h3>
          <StatusBadge status={ministerio.status} />
        </div>
      </div>

      <div className="ministerio-card-details">
        <div className="detail-item">
          <FaUser className="detail-icon" />
          <div>
            <span className="detail-label">Líder:</span>
            <span className="detail-value">{ministerio.lider?.nome || 'Não definido'}</span>
          </div>
        </div>
        
        <div className="detail-item">
          <FaUsers className="detail-icon" />
          <div>
            <span className="detail-label">Membros:</span>
            <span className="detail-value">
              {ministerio.membrosCount || 0} membro(s)
            </span>
          </div>
        </div>
      </div>

      <div className="ministerio-card-footer">
        <span className="ministerio-since">
          Criado em {formatDate(ministerio.dataCadastro)}
        </span>
        <div className="ministerio-card-actions">
          <button 
            className="btn btn-icon btn-sm" 
            onClick={onEdit}
            title="Editar"
          >
            <FaEdit />
          </button>
          {ministerio.status === 'ATIVO' ? (
            <button 
              className="btn btn-icon btn-sm btn-danger" 
              onClick={onInativar}
              title="Inativar"
            >
              <FaTrash />
            </button>
          ) : (
            <button 
              className="btn btn-icon btn-sm btn-success" 
              onClick={onReativar}
              title="Reativar"
            >
              <FaUserCheck />
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default MinisterioCard;