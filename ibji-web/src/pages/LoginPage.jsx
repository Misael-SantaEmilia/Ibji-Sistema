import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEnvelope, FaLock, FaSignInAlt } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';

const LoginPage = () => {
  const { login } = useAuth();
  const [credentials, setCredentials] = useState({
    email: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!credentials.email || !credentials.password) {
      toast.error('Preencha todos os campos');
      return;
    }

    setLoading(true);
    try {
      await login(credentials);
      toast.success('Login realizado com sucesso!');
      navigate('/');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Credenciais inválidas');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-header">
          <h1 className="login-logo">IBJI</h1>
          <p className="login-subtitle">Sistema de Gestão Eclesiástica</p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email" className="form-label">
              <FaEnvelope /> Email
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className="form-input"
              value={credentials.email}
              onChange={handleChange}
              placeholder="seu@email.com"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password" className="form-label">
              <FaLock /> Senha
            </label>
            <input
              type="password"
              id="password"
              name="password"
              className="form-input"
              value={credentials.password}
              onChange={handleChange}
              placeholder="Sua senha"
              required
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary btn-block"
            disabled={loading}
          >
            <FaSignInAlt />
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <div className="login-footer">
          <p>Versão 1.0.0</p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;