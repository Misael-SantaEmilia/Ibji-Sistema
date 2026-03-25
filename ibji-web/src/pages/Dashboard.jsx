import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaUsers, FaChurch, FaCalendarAlt, FaPray, FaBirthdayCake } from 'react-icons/fa';
import membroService from '../api/membroService';
import ministerioService from '../api/ministerioService';
import Loading from '../components/common/Loading';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalMembros: 0,
    membrosAtivos: 0,
    totalMinisterios: 0,
    aniversariantesMes: 0,
  });
  const [loading, setLoading] = useState(true);
  const [aniversariantes, setAniversariantes] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      const [membrosResponse, ministeriosResponse, aniversariantesResponse] = await Promise.all([
        membroService.findAll({ size: 1 }),
        ministerioService.findAll({ size: 1 }),
        membroService.findAniversariantes('M'),
      ]);

      setStats({
        totalMembros: membrosResponse.totalElements || 0,
        membrosAtivos: membrosResponse.content?.filter(m => m.status === 'ATIVO').length || 0,
        totalMinisterios: ministeriosResponse.totalElements || 0,
        aniversariantesMes: aniversariantesResponse.length || 0,
      });

      setAniversariantes(aniversariantesResponse.slice(0, 5) || []);
    } catch (error) {
      console.error('Erro ao carregar dados do dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Loading message="Carregando dashboard..." />;
  }

  return (
    <div className="dashboard">
      <h1 className="dashboard-title">Dashboard</h1>
      
      <div className="dashboard-stats">
        <div className="stat-card">
          <div className="stat-icon">
            <FaUsers />
          </div>
          <div className="stat-info">
            <span className="stat-value">{stats.totalMembros}</span>
            <span className="stat-label">Total de Membros</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon active">
            <FaUsers />
          </div>
          <div className="stat-info">
            <span className="stat-value">{stats.membrosAtivos}</span>
            <span className="stat-label">Membros Ativos</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">
            <FaChurch />
          </div>
          <div className="stat-info">
            <span className="stat-value">{stats.totalMinisterios}</span>
            <span className="stat-label">Ministérios</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon birthday">
            <FaBirthdayCake />
          </div>
          <div className="stat-info">
            <span className="stat-value">{stats.aniversariantesMes}</span>
            <span className="stat-label">Aniversariantes do Mês</span>
          </div>
        </div>
      </div>

      <div className="dashboard-content">
        <div className="dashboard-section">
          <div className="section-header">
            <h2 className="section-title">
              <FaBirthdayCake /> Próximos Aniversariantes
            </h2>
            <Link to="/aniversariantes" className="section-link">
              Ver todos
            </Link>
          </div>
          
          <div className="section-content">
            {aniversariantes.length === 0 ? (
              <p className="empty-message">Nenhum aniversariante este mês.</p>
            ) : (
              <ul className="aniversariantes-list">
                {aniversariantes.map((membro) => (
                  <li key={membro.id} className="aniversariante-item">
                    <div className="aniversariante-avatar">
                      {membro.fotoUrl ? (
                        <img src={membro.fotoUrl} alt={membro.nome} />
                      ) : (
                        <span>{membro.nome.charAt(0)}</span>
                      )}
                    </div>
                    <div className="aniversariante-info">
                      <span className="aniversariante-name">{membro.nome}</span>
                      <span className="aniversariante-date">
                        {new Date(membro.dataNascimento).toLocaleDateString('pt-BR', { day: '2-digit', month: 'long' })}
                      </span>
                    </div>
                    <span className="aniversariante-age">{membro.idade} anos</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        <div className="dashboard-section">
          <div className="section-header">
            <h2 className="section-title">
              <FaPray /> Acesso Rápido
            </h2>
          </div>
          
          <div className="quick-access">
            <Link to="/membros" className="quick-access-item">
              <FaUsers />
              <span>Membros</span>
            </Link>
            <Link to="/ministerios" className="quick-access-item">
              <FaChurch />
              <span>Ministérios</span>
            </Link>
            <Link to="/escalas" className="quick-access-item">
              <FaCalendarAlt />
              <span>Escalas</span>
            </Link>
            <Link to="/pedidos-oracao" className="quick-access-item">
              <FaPray />
              <span>Pedidos de Oração</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;