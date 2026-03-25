import apiClient from './apiClient';

const aniversarianteService = {
  // Buscar aniversariantes
  // tipo: D (Dia), S (Semana), M (Mês)
  async findByTipo(tipo = 'M') {
    const response = await apiClient.get('/aniversariantes', { params: { tipo } });
    return response.data;
  },
};

export default aniversarianteService;
