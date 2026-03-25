import React from 'react';
import { Link } from 'react-router-dom';

const Header = ({ title }) => {
  return (
    <header className="header">
      <div className="header-content">
        <h1 className="header-title">{title || 'IBJI - Gestão Eclesiástica'}</h1>
        <nav className="header-nav">
          <Link to="/" className="nav-link">Dashboard</Link>
          <Link to="/membros" className="nav-link">Membros</Link>
          <Link to="/ministerios" className="nav-link">Ministérios</Link>
          <Link to="/escalas" className="nav-link">Escalas</Link>
          <Link to="/pedidos-oracao" className="nav-link">Pedidos de Oração</Link>
          <Link to="/recados" className="nav-link">Recados</Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;
