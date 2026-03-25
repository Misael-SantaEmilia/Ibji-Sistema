import React, { useState, useEffect } from 'react';
import { FaSave, FaTimes } from 'react-icons/fa';

const MembroForm = ({ initialData, onSave, onCancel }) => {
  const [formData, setFormData] = useState({
    nome: '',
    cpf: '',
    email: '',
    telefone: '',
    dataNascimento: '',
    endereco: '',
    cidade: '',
    estado: '',
    cep: '',
    nivelAcesso: 'MEMBRO',
    fotoUrl: '',
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (initialData) {
      setFormData({
        nome: initialData.nome || '',
        cpf: initialData.cpf || '',
        email: initialData.email || '',
        telefone: initialData.telefone || '',
        dataNascimento: initialData.dataNascimento || '',
        endereco: initialData.endereco || '',
        cidade: initialData.cidade || '',
        estado: initialData.estado || '',
        cep: initialData.cep || '',
        nivelAcesso: initialData.nivelAcesso || 'MEMBRO',
        fotoUrl: initialData.fotoUrl || '',
      });
    }
  }, [initialData]);

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

    if (!formData.nome.trim()) {
      newErrors.nome = 'Nome é obrigatório';
    } else if (formData.nome.length > 150) {
      newErrors.nome = 'Nome deve ter no máximo 150 caracteres';
    }

    if (formData.cpf && !/^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/.test(formData.cpf)) {
      newErrors.cpf = 'CPF inválido';
    }

    if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Email inválido';
    }

    if (formData.estado && !/^[A-Z]{2}$/.test(formData.estado)) {
      newErrors.estado = 'Estado deve ter 2 letras maiúsculas';
    }

    if (formData.cep && !/^\d{5}-?\d{3}$/.test(formData.cep)) {
      newErrors.cep = 'CEP inválido';
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
      const dataToSave = { ...formData };
      
      if (!dataToSave.cpf) delete dataToSave.cpf;
      if (!dataToSave.email) delete dataToSave.email;
      if (!dataToSave.dataNascimento) delete dataToSave.dataNascimento;
      
      await onSave(dataToSave);
    } catch (error) {
      console.error('Erro ao salvar:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="form" onSubmit={handleSubmit}>
      <div className="form-row">
        <div className="form-group">
          <label htmlFor="nome" className="form-label">
            Nome *
          </label>
          <input
            type="text"
            id="nome"
            name="nome"
            className={`form-input ${errors.nome ? 'error' : ''}`}
            value={formData.nome}
            onChange={handleChange}
            placeholder="Nome completo"
            maxLength={150}
          />
          {errors.nome && <span className="form-error">{errors.nome}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="cpf" className="form-label">
            CPF
          </label>
          <input
            type="text"
            id="cpf"
            name="cpf"
            className={`form-input ${errors.cpf ? 'error' : ''}`}
            value={formData.cpf}
            onChange={handleChange}
            placeholder="000.000.000-00"
          />
          {errors.cpf && <span className="form-error">{errors.cpf}</span>}
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="email" className="form-label">
            Email
          </label>
          <input
            type="email"
            id="email"
            name="email"
            className={`form-input ${errors.email ? 'error' : ''}`}
            value={formData.email}
            onChange={handleChange}
            placeholder="email@exemplo.com"
          />
          {errors.email && <span className="form-error">{errors.email}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="telefone" className="form-label">
            Telefone
          </label>
          <input
            type="text"
            id="telefone"
            name="telefone"
            className="form-input"
            value={formData.telefone}
            onChange={handleChange}
            placeholder="(00) 00000-0000"
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="dataNascimento" className="form-label">
            Data de Nascimento
          </label>
          <input
            type="date"
            id="dataNascimento"
            name="dataNascimento"
            className="form-input"
            value={formData.dataNascimento}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label htmlFor="nivelAcesso" className="form-label">
            Nível de Acesso
          </label>
          <select
            id="nivelAcesso"
            name="nivelAcesso"
            className="form-select"
            value={formData.nivelAcesso}
            onChange={handleChange}
          >
            <option value="MEMBRO">Membro</option>
            <option value="LIDER">Líder</option>
            <option value="ADM">Administrador</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="endereco" className="form-label">
          Endereço
        </label>
        <input
          type="text"
          id="endereco"
          name="endereco"
          className="form-input"
          value={formData.endereco}
          onChange={handleChange}
          placeholder="Rua, número, bairro"
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="cidade" className="form-label">
            Cidade
          </label>
          <input
            type="text"
            id="cidade"
            name="cidade"
            className="form-input"
            value={formData.cidade}
            onChange={handleChange}
          />
        </div>

        <div className="form-group form-group-small">
          <label htmlFor="estado" className="form-label">
            Estado
          </label>
          <input
            type="text"
            id="estado"
            name="estado"
            className={`form-input ${errors.estado ? 'error' : ''}`}
            value={formData.estado}
            onChange={handleChange}
            placeholder="UF"
            maxLength={2}
            style={{ textTransform: 'uppercase' }}
          />
          {errors.estado && <span className="form-error">{errors.estado}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="cep" className="form-label">
            CEP
          </label>
          <input
            type="text"
            id="cep"
            name="cep"
            className={`form-input ${errors.cep ? 'error' : ''}`}
            value={formData.cep}
            onChange={handleChange}
            placeholder="00000-000"
          />
          {errors.cep && <span className="form-error">{errors.cep}</span>}
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="fotoUrl" className="form-label">
          URL da Foto
        </label>
        <input
          type="text"
          id="fotoUrl"
          name="fotoUrl"
          className="form-input"
          value={formData.fotoUrl}
          onChange={handleChange}
          placeholder="https://..."
        />
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

export default MembroForm;