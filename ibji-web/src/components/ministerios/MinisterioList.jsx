import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import ministerioService from '../../api/ministerioService';
import MinisterioCard from './MinisterioCard';
import MinisterioForm from './MinisterioForm';
import Pagination from '../common/Pagination';
import Loading from '../common/Loading';
import Modal from '../common/Modal';
import ConfirmDialog from '../common/ConfirmDialog';
import { FaPlus, FaSync } from 'react-icons/fa';

const MinisterioList = () => {
  const [ministerios, setMinisterios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  
  const [showForm, setShowForm] = useState(false);
  const [editingMinisterio, setEditingMinisterio] = useState(null);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [ministerioToDelete, setMinisterioToDelete] = useState(null);

  const fetchMinisterios = async () => {
    setLoading(true);
    try {
      const params = {
        page,
        size: 20,
        sortBy: 'descricao',
        sortDir: 'asc',
      };
      
      const response = await ministerioService.findAll(params);
      setMinisterios(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (error) {
      toast.error('Erro ao carregar ministérios');
      console.error('Erro:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMinisterios();
  }, [page]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleRefresh = () => {
    fetchMinisterios();
  };

  const handleCreate = () => {
    setEditingMinisterio(null);
    setShowForm(true);
  };

  const handleEdit = (ministerio) => {
    setEditingMinisterio(ministerio);
    setShowForm(true);
  };

  const handleFormClose = () => {
    setShowForm(false);
    setEditingMinisterio(null);
  };

  const handleFormSave = async (ministerioData) => {
    try {
      if (editingMinisterio) {
        await ministerioService.update(editingMinisterio.id, ministerioData);
        toast.success('Ministério atualizado com sucesso!');
      } else {
        await ministerioService.create(ministerioData);
        toast.success('Ministério criado com sucesso!');
      }
      handleFormClose();
      fetchMinisterios();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao salvar ministério');
      throw error;
    }
  };

  const handleInativarClick = (ministerio) => {
    setMinisterioToDelete(ministerio);
    setShowConfirmDialog(true);
  };

  const handleConfirmInativar = async () => {
    try {
      await ministerioService.inativar(ministerioToDelete.id);
      toast.success('Ministério inativado com sucesso!');
      setShowConfirmDialog(false);
      setMinisterioToDelete(null);
      fetchMinisterios();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao inativar ministério');
    }
  };

  const handleReativar = async (ministerio) => {
    try {
      await ministerioService.reativar(ministerio.id);
      toast.success('Ministério reativado com sucesso!');
      fetchMinisterios();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erro ao reativar ministério');
    }
  };

  if (loading && ministerios.length === 0) {
    return <Loading message="Carregando ministérios..." />;
  }

  return (
    <div className="ministerio-list-container">
      <div className="ministerio-list-header">
        <h2 className="ministerio-list-title">
          Ministérios <span className="badge">{totalElements}</span>
        </h2>
        <div className="ministerio-list-actions">
          <button className="btn btn-icon" onClick={handleRefresh} disabled={loading}>
            <FaSync className={loading ? 'spin' : ''} />
          </button>
          <button className="btn btn-primary" onClick={handleCreate}>
            <FaPlus /> Novo Ministério
          </button>
        </div>
      </div>

      <div className="ministerio-list">
        {ministerios.length === 0 ? (
          <div className="empty-state">
            <p>Nenhum ministério encontrado.</p>
          </div>
        ) : (
          ministerios.map((ministerio) => (
            <MinisterioCard
              key={ministerio.id}
              ministerio={ministerio}
              onEdit={() => handleEdit(ministerio)}
              onInativar={() => handleInativarClick(ministerio)}
              onReativar={() => handleReativar(ministerio)}
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
        title={editingMinisterio ? 'Editar Ministério' : 'Novo Ministério'}
      >
        <MinisterioForm
          initialData={editingMinisterio}
          onSave={handleFormSave}
          onCancel={handleFormClose}
        />
      </Modal>

      <ConfirmDialog
        isOpen={showConfirmDialog}
        onClose={() => {
          setShowConfirmDialog(false);
          setMinisterioToDelete(null);
        }}
        onConfirm={handleConfirmInativar}
        title="Confirmar Inativação"
        message={`Deseja inativar o ministério "${ministerioToDelete?.descricao}"?`}
      />
    </div>
  );
};

export default MinisterioList;