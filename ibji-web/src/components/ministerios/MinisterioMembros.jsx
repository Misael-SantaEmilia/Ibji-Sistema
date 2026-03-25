import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { FaUserPlus, FaUserMinus, FaSearch } from 'react-icons/fa';
import ministerioService from '../../api/ministerioService';
import membroService from '../../api/membroService';
import Loading from '../common/Loading';

const MinisterioMembros = ({ ministerioId, onClose }) => {
  const [membrosMinisterio, setMembrosMinisterio] = useState([]);
  const [membrosDisponiveis, setMembrosDisponiveis] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    if (ministerioId) {
      fetchData();
    }
  }, [ministerioId]);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [membrosMin, todosMembros] = await Promise.all([
        ministerioService.findMembros(ministerioId),
        membroService.findAll({ status: 'ATIVO', size: 1000 }),
      ]);

      setMembrosMinisterio(membrosMin || []);
      
      const idsNoMinisterio = new Set((membrosMin || []).map(m => m.id));
      const disponiveis = (todosMembros.content || []).filter(
        m => !idsNoMinisterio.has(m.id)
      );
      setMembrosDisponiveis(disponiveis);
    } catch (error) {
      toast.error('Erro ao carregar dados');
      console.error('Erro:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdicionar = async (membro) => {
    try {
      await ministerioService.adicionarMembro(ministerioId, membro.id);
      toast.success(`${membro.nome} adicionado ao ministério!`);
      fetchData();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao adicionar membro');
    }
  };

  const handleRemover = async (membro) => {
    try {
      await ministerioService.removerMembro(ministerioId, membro.id);
      toast.success(`${membro.nome} removido do ministério!`);
      fetchData();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao remover membro');
    }
  };

  const filteredDisponiveis = membrosDisponiveis.filter(m =>
    m.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <Loading message="Carregando membros..." />;
  }

  return (
    <div className="ministerio-membros">
      <div className="ministerio-membros-header">
        <h4>Membros do Ministério</h4>
        <span className="badge">{membrosMinisterio.length}</span>
      </div>

      <div className="ministerio-membros-list">
        {membrosMinisterio.length === 0 ? (
          <p className="empty-message">Nenhum membro neste ministério.</p>
        ) : (
          membrosMinisterio.map((membro) => (
            <div key={membro.id} className="membro-item">
              <div className="membro-item-info">
                <span className="membro-item-name">{membro.nome}</span>
                <span className="membro-item-email">{membro.email}</span>
              </div>
              <button
                className="btn btn-icon btn-sm btn-danger"
                onClick={() => handleRemover(membro)}
                title="Remover do ministério"
              >
                <FaUserMinus />
              </button>
            </div>
          ))
        )}
      </div>

      <div className="ministerio-membros-divider" />

      <div className="ministerio-membros-header">
        <h4>Adicionar Membros</h4>
      </div>

      <div className="search-input-container">
        <FaSearch className="search-icon" />
        <input
          type="text"
          className="search-input"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Buscar membros..."
        />
      </div>

      <div className="ministerio-membros-list">
        {filteredDisponiveis.length === 0 ? (
          <p className="empty-message">
            {searchTerm ? 'Nenhum membro encontrado.' : 'Todos os membros ativos já estão no ministério.'}
          </p>
        ) : (
          filteredDisponiveis.map((membro) => (
            <div key={membro.id} className="membro-item">
              <div className="membro-item-info">
                <span className="membro-item-name">{membro.nome}</span>
                <span className="membro-item-email">{membro.email}</span>
              </div>
              <button
                className="btn btn-icon btn-sm btn-success"
                onClick={() => handleAdicionar(membro)}
                title="Adicionar ao ministério"
              >
                <FaUserPlus />
              </button>
            </div>
          ))
        )}
      </div>

      <div className="form-actions">
        <button className="btn btn-secondary" onClick={onClose}>
          Fechar
        </button>
      </div>
    </div>
  );
};

export default MinisterioMembros;