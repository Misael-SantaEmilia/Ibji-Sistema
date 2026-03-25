import React from 'react';
import { FaEdit, FaTrash, FaUserCheck, FaEnvelope, FaPhone, FaMapMarkerAlt } from 'react-icons/fa';
import StatusBadge from '../common/StatusBadge';

const MembroCard = ({ membro, onEdit, onInativar, onReativar }) => {
  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  };

  const formatPhone = (phone) => {
    if (!phone) return null;
    return phone;
  };

  return (
    <div className={`membro-card ${membro.status === 'INATIVO' ? 'inactive' : ''}`}>
      <div className="membro-card-header">
        <div className="membro-card-avatar">
          {membro.fotoUrl ? (
            <img src={membro.fotoUrl} alt={membro.nome} />
          ) : (
            <div className="avatar-placeholder">
              {membro.nome.charAt(0).toUpperCase()}
            </div>
          )}
        </div>
        <div className="membro-card-info">
          <h3 className="membro-card-name">{membro.nome}</h3>
          <div className="membro-card-badges">
            <StatusBadge status={membro.status} />
            <span className={`nivel-badge nivel-${membro.nivelAcesso?.toLowerCase()}`}>
              {membro.nivelAcesso}
            </span>
          </div>
        </div>
      </div>

      <div className="membro-card-details">
        {membro.email && (
          <div className="detail-item">
            <FaEnvelope className="detail-icon" />
            <span>{membro.email}</span>
          </div>
        )}
        {membro.telefone && (
          <div className="detail-item">
            <FaPhone className="detail-icon" />
            <span>{formatPhone(membro.telefone)}</span>
          </div>
        )}
        {(membro.cidade || membro.estado) && (
          <div className="detail-item">
            <FaMapMarkerAlt className="detail-icon" />
            <span>
              {[membro.cidade, membro.estado].filter(Boolean).join(', ')}
            </span>
          </div>
        )}
      </div>

      <div className="membro-card-footer">
        <span className="membro-since">
          Membro desde {formatDate(membro.dataCadastro)}
        </span>
        <div className="membro-card-actions">
          <button 
            className="btn btn-icon btn-sm" 
            onClick={onEdit}
            title="Editar"
          >
            <FaEdit />
          </button>
          {membro.status === 'ATIVO' ? (
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

export default MembroCard;