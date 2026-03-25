import apiClient from './apiClient';

const escalaService = {
  // Listar todas as escalas futuras
  async findAll() {
    const response = await apiClient.get('/escalas');
    return response.data;
  },

  // Buscar por ID
  async findById(id) {
    const response = await apiClient.get(`/escalas/${id}`);
    return response.data;
  },

  // Buscar por ministério
  async findByMinisterio(ministerioId) {
    const response = await apiClient.get(`/escalas/ministerio/${ministerioId}`);
    return response.data;
  },

  // Buscar por período
  async findByPeriodo(inicio, fim) {
    const response = await apiClient.get('/escalas/periodo', {
      params: { inicio, fim },
    });
    return response.data;
  },

  // Criar escala
  async create(data, criadoPorId) {
    const response = await apiClient.post('/escalas', data, {
      params: { criadoPorId },
    });
    return response.data;
  },

  // Atualizar escala
  async update(id, data) {
    const response = await apiClient.put(`/escalas/${id}`, data);
    return response.data;
  },

  // Deletar escala
  async delete(id) {
    await apiClient.delete(`/escalas/${id}`);
  },
};

export default escalaService;
