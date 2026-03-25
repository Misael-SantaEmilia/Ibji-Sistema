import React from 'react';
import { FaFilter, FaTimes } from 'react-icons/fa';
import SearchInput from '../common/SearchInput';

const MembroFilter = ({ filters, onFilterChange }) => {
  const handleStatusChange = (e) => {
    const value = e.target.value;
    onFilterChange({
      ...filters,
      status: value === '' ? null : value,
    });
  };

  const handleNomeChange = (value) => {
    onFilterChange({
      ...filters,
      nome: value,
    });
  };

  const handleClearFilters = () => {
    onFilterChange({
      status: null,
      nome: '',
    });
  };

  const hasActiveFilters = filters.status !== null || filters.nome !== '';

  return (
    <div className="filter-container">
      <div className="filter-header">
        <FaFilter className="filter-icon" />
        <span>Filtros</span>
        {hasActiveFilters && (
          <button className="btn btn-link btn-sm" onClick={handleClearFilters}>
            <FaTimes /> Limpar filtros
          </button>
        )}
      </div>
      
      <div className="filter-content">
        <div className="filter-group">
          <label className="filter-label">Status</label>
          <select
            className="filter-select"
            value={filters.status || ''}
            onChange={handleStatusChange}
          >
            <option value="">Todos</option>
            <option value="ATIVO">Ativo</option>
            <option value="INATIVO">Inativo</option>
          </select>
        </div>

        <div className="filter-group filter-search">
          <label className="filter-label">Nome</label>
          <SearchInput
            value={filters.nome}
            onChange={handleNomeChange}
            placeholder="Buscar por nome..."
            onClear={() => handleNomeChange('')}
          />
        </div>
      </div>
    </div>
  );
};

export default MembroFilter;