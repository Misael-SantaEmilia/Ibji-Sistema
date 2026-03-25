import React, { useState, useEffect } from 'react';
import { FaSave, FaTimes } from 'react-icons/fa';
import membroService from '../../api/membroService';

const MinisterioForm = ({ initialData, onSave, onCancel }) => {
  const [formData, setFormData] = useState({
    descricao: '',
    liderId: '',
  });

  const [lideres, setLideres] = useState([]);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [loadingLideres, setLoadingLideres] = useState(true);

  useEffect(() => {
    fetchLideres();
  }, []);

  useEffect(() => {
    if (initialData) {
      setFormData({
        descricao: initialData.descricao || '',
        liderId: initialData.lider?.id || '',
      });
    }
  }, [initialData]);

  const fetchLideres = async () => {
    try {
      const response = await membroService.findAll({ nivelAcesso: 'LIDER', size: 1000 });
      setLideres(response.content || []);
    } catch (error) {
      console.error('Erro ao carregar líderes:', error);
    } finally {
      setLoadingLideres(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: '',
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.descricao.trim()) {
      newErrors.descricao = 'Descrição é obrigatória';
    } else if (formData.descricao.length > 80) {
      newErrors.descricao = 'Descrição deve ter no máximo 80 caracteres';
    }

    if (!formData.liderId) {
      newErrors.liderId = 'Líder é obrigatório';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setLoading(true);
    try {
      const dataToSave = {
        descricao: formData.descricao.trim(),
        liderId: parseInt(formData.liderId, 10),
      };
      
      await onSave(dataToSave);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="descricao" className="form-label">
          Descrição *
        </label>
        <input
          type="text"
          id="descricao"
          name="descricao"
          className={`form-input ${errors.descricao ? 'error' : ''}`}
          value={formData.descricao}
          onChange={handleChange}
          placeholder="Nome do ministério"
          maxLength={80}
        />
        {errors.descricao && <span className="form-error">{errors.descricao}</span>}
        <span className="form-hint">
          {formData.descricao.length}/80 caracteres
        </span>
      </div>

      <div className="form-group">
        <label htmlFor="liderId" className="form-label">
          Líder Responsável *
        </label>
        <select
          id="liderId"
          name="liderId"
          className={`form-select ${errors.liderId ? 'error' : ''}`}
          value={formData.liderId}
          onChange={handleChange}
          disabled={loadingLideres}
        >
          <option value="">Selecione um líder...</option>
          {lideres.map((lider) => (
            <option key={lider.id} value={lider.id}>
              {lider.nome}
            </option>
          ))}
        </select>
        {errors.liderId && <span className="form-error">{errors.liderId}</span>}
      </div>

      <div className="form-actions">
        <button type="button" className="btn btn-secondary" onClick={onCancel}>
          <FaTimes /> Cancelar
        </button>
        <button type="submit" className="btn btn-primary" disabled={loading}>
          <FaSave /> {loading ? 'Salvando...' : 'Salvar'}
        </button>
      </div>
    </form>
  );
};

export default MinisterioForm;