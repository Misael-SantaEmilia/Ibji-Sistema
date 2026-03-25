import React, { useState, useEffect } from 'react';
import { FaBirthdayCake, FaCalendarDay, FaCalendarWeek, FaCalendarAlt } from 'react-icons/fa';
import membroService from '../api/membroService';
import Loading from '../components/common/Loading';

const AniversariantesPage = () => {
  const [aniversariantes, setAniversariantes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('M'); // D=Dia, S=Semana, M=Mês

  useEffect(() => {
    fetchAniversariantes();
  }, [filter]);

  const fetchAniversariantes = async () => {
    setLoading(true);
    try {
      const response = await membroService.findAniversariantes(filter);
      setAniversariantes(response || []);
    } catch (error) {
      console.error('Erro ao carregar aniversariantes:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: 'long' });
  };

  const filters = [
    { value: 'D', label: 'Hoje', icon: FaCalendarDay },
    { value: 'S', label: 'Esta Semana', icon: FaCalendarWeek },
    { value: 'M', label: 'Este Mês', icon: FaCalendarAlt },
  ];

  if (loading) {
    return <Loading message="Carregando aniversariantes..." />;
  }

  return (
    <div className="page aniversariantes-page">
      <div className="page-header">
        <h1 className="page-title">
          <FaBirthdayCake /> Aniversariantes
        </h1>
      </div>

      <div className="filter-tabs">
        {filters.map((f) => (
          <button
            key={f.value}
            className={`filter-tab ${filter === f.value ? 'active' : ''}`}
            onClick={() => setFilter(f.value)}
          >
            <f.icon />
            <span>{f.label}</span>
          </button>
        ))}
      </div>

      <div className="aniversariantes-grid">
        {aniversariantes.length === 0 ? (
          <div className="empty-state">
            <FaBirthdayCake className="empty-icon" />
            <p>Nenhum aniversariante{filter === 'D' ? ' hoje' : filter === 'S' ? ' esta semana' : ' este mês'}.</p>
          </div>
        ) : (
          aniversariantes.map((membro) => (
            <div key={membro.id} className="aniversariante-card">
              <div className="aniversariante-card-avatar">
                {membro.fotoUrl ? (
                  <img src={membro.fotoUrl} alt={membro.nome} />
                ) : (
                  <span>{membro.nome.charAt(0)}</span>
                )}
              </div>
              <div className="aniversariante-card-info">
                <h3 className="aniversariante-card-name">{membro.nome}</h3>
                <p className="aniversariante-card-date">
                  <FaBirthdayCake /> {formatDate(membro.dataNascimento)}
                </p>
                <p className="aniversariante-card-age">{membro.idade} anos</p>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default AniversariantesPage;