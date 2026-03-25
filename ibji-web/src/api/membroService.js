import apiClient from './apiClient';

const membroService = {
  // Listar todos os membros com paginação e filtros
  async findAll(params = {}) {
    const response = await apiClient.get('/membresia', { params });
    return response.data;
  },

  // Buscar membro por ID
  async findById(id) {
    const response = await apiClient.get(`/membresia/${id}`);
    return response.data;
  },

  // Criar novo membro
  async create(data) {
    const response = await apiClient.post('/membresia', data);
    return response.data;
  },

  // Atualizar membro
  async update(id, data) {
    const response = await apiClient.put(`/membresia/${id}`, data);
    return response.data;
  },

  // Inativar membro
  async inativar(id) {
    const response = await apiClient.patch(`/membresia/${id}/inativar`);
    return response.data;
  },

  // Reativar membro
  async reativar(id) {
    const response = await apiClient.patch(`/membresia/${id}/reativar`);
    return response.data;
  },
};

export default membroService;
