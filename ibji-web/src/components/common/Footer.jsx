import React from 'react';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="footer-content">
        <p className="footer-copyright">
          &copy; {currentYear} IBJI - Sistema de Gestão Eclesiástica
        </p>
        <p className="footer-version">Versão 1.0.0</p>
      </div>
    </footer>
  );
};

export default Footer;