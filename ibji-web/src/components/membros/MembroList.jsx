import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import membroService from '../../api/membroService';
import MembroCard from './MembroCard';
import MembroFilter from './MembroFilter';
import MembroForm from './MembroForm';
import Pagination from '../common/Pagination';
import Loading from '../common/Loading';
import Modal from '../common/Modal';
import ConfirmDialog from '../common/ConfirmDialog';
import { FaPlus, FaSync } from 'react-icons/fa';

const MembroList = () => {
  const [membros, setMembros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [filters, setFilters] = useState({
    status: null,
    nome: '',
  });
  
  const [showForm, setShowForm] = useState(false);
  const [editingMembro, setEditingMembro] = useState(null);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [membroToDelete, setMembroToDelete] = useState(null);

  const fetchMembros = async () => {
    setLoading(true);
    try {
      const params = {
        page,
        size: 20,
        sortBy: 'nome',
        sortDir: 'asc',
        ...filters,
      };
      
      const response = await membroService.findAll(params);
      setMembros(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (error) {
      toast.error('Erro ao carregar membros');
      console.error('Erro:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMembros();
  }, [page, filters]);

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPage(0);
  };

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleRefresh = () => {
    fetchMembros();
  };

  const handleCreate = () => {
    setEditingMembro(null);
    setShowForm(true);
  };

  const handleEdit = (membro) => {
    setEditingMembro(membro);
    setShowForm(true);
  };

  const handleFormClose = () => {
    setShowForm(false);
    setEditingMembro(null);
  };

  const handleFormSave = async (membroData) => {
    try {
      if (editingMembro) {
        await membroService.update(editingMembro.id, membroData);
        toast.success('Membro atualizado com sucesso!');
      } else {
        await membroService.create(membroData);
        toast.success('Membro criado com sucesso!');
      }
      handleFormClose();
      fetchMembros();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao salvar membro');
      throw error;
    }
  };

  const handleInativarClick = (membro) => {
    setMembroToDelete(membro);
    setShowConfirmDialog(true);
  };

  const handleConfirmInativar = async () => {
    try {
      await membroService.inativar(membroToDelete.id);
      toast.success('Membro inativado com sucesso!');
      setShowConfirmDialog(false);
      setMembroToDelete(null);
      fetchMembros();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao inativar membro');
    }
  };

  const handleReativar = async (membro) => {
    try {
      await membroService.reativar(membro.id);
      toast.success('Membro reativado com sucesso!');
      fetchMembros();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao reativar membro');
    }
  };

  if (loading && membros.length === 0) {
    return <Loading message="Carregando membros..." />;
  }

  return (
    <div className="membro-list-container">
      <div className="membro-list-header">
        <h2 className="membro-list-title">
          Membros <span className="badge">{totalElements}</span>
        </h2>
        <div className="membro-list-actions">
          <button className="btn btn-icon" onClick={handleRefresh} disabled={loading}>
            <FaSync className={loading ? 'spin' : ''} />
          </button>
          <button className="btn btn-primary" onClick={handleCreate}>
            <FaPlus /> Novo Membro
          </button>
        </div>
      </div>

      <MembroFilter filters={filters} onFilterChange={handleFilterChange} />

      <div className="membro-list">
        {membros.length === 0 ? (
          <div className="empty-state">
            <p>Nenhum membro encontrado.</p>
          </div>
        ) : (
          membros.map((membro) => (
            <MembroCard
              key={membro.id}
              membro={membro}
              onEdit={() => handleEdit(membro)}
              onInativar={() => handleInativarClick(membro)}
              onReativar={() => handleReativar(membro)}
            />
          ))
        )}
      </div>

      {totalPages > 1 && (
        <Pagination
          page={page}
          totalPages={totalPages}
          onPageChange={handlePageChange}
        />
      )}

      <Modal
        isOpen={showForm}
        onClose={handleFormClose}
        title={editingMembro ? 'Editar Membro' : 'Novo Membro'}
      >
        <MembroForm
          initialData={editingMembro}
          onSave={handleFormSave}
          onCancel={handleFormClose}
        />
      </Modal>

      <ConfirmDialog
        isOpen={showConfirmDialog}
        onClose={() => {
          setShowConfirmDialog(false);
          setMembroToDelete(null);
        }}
        onConfirm={handleConfirmInativar}
        title="Confirmar Inativação"
        message={`Deseja inativar o membro "${membroToDelete?.nome}"?`}
      />
    </div>
  );
};

export default MembroList;