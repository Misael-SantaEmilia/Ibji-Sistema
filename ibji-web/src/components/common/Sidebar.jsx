import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  FaHome, 
  FaUsers, 
  FaChurch, 
  FaCalendarAlt, 
  FaPray, 
  FaEnvelope,
  FaBirthdayCake,
  FaSignOutAlt 
} from 'react-icons/fa';

const Sidebar = ({ user, onLogout }) => {
  const menuItems = [
    { path: '/', icon: FaHome, label: 'Dashboard', roles: ['ADM', 'LIDER', 'MEMBRO'] },
    { path: '/membros', icon: FaUsers, label: 'Membros', roles: ['ADM'] },
    { path: '/ministerios', icon: FaChurch, label: 'Ministérios', roles: ['ADM', 'LIDER'] },
    { path: '/escalas', icon: FaCalendarAlt, label: 'Escalas', roles: ['ADM', 'LIDER'] },
    { path: '/pedidos-oracao', icon: FaPray, label: 'Pedidos de Oração', roles: ['ADM'] },
    { path: '/recados', icon: FaEnvelope, label: 'Recados', roles: ['ADM', 'LIDER', 'MEMBRO'] },
    { path: '/aniversariantes', icon: FaBirthdayCake, label: 'Aniversariantes', roles: ['ADM', 'LIDER', 'MEMBRO'] },
  ];

  const filteredMenuItems = menuItems.filter(item => 
    item.roles.includes(user?.nivelAcesso)
  );

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h2 className="sidebar-logo">IBJI</h2>
        <p className="sidebar-subtitle">Gestão Eclesiástica</p>
      </div>
      
      <nav className="sidebar-nav">
        {filteredMenuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => 
              `sidebar-link ${isActive ? 'active' : ''}`
            }
            end={item.path === '/'}
          >
            <item.icon className="sidebar-icon" />
            <span className="sidebar-label">{item.label}</span>
          </NavLink>
        ))}
      </nav>
      
      <div className="sidebar-footer">
        <div className="sidebar-user">
          <p className="sidebar-user-name">{user?.nome}</p>
          <p className="sidebar-user-role">{user?.nivelAcesso}</p>
        </div>
        <button className="sidebar-logout" onClick={onLogout}>
          <FaSignOutAlt />
          <span>Sair</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;