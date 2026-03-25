import apiClient from './apiClient';

const ministerioService = {
  // Listar todos os ministérios
  async findAll(params = {}) {
    const response = await apiClient.get('/ministerios', { params });
    return response.data;
  },

  // Buscar ministério por ID
  async findById(id) {
    const response = await apiClient.get(`/ministerios/${id}`);
    return response.data;
  },

  // Criar novo ministério
  async create(data) {
    const response = await apiClient.post('/ministerios', data);
    return response.data;
  },

  // Atualizar ministério
  async update(id, data) {
    const response = await apiClient.put(`/ministerios/${id}`, data);
    return response.data;
  },

  // Inativar ministério
  async inativar(id) {
    const response = await apiClient.patch(`/ministerios/${id}/inativar`);
    return response.data;
  },

  // Adicionar membro ao ministério
  async adicionarMembro(ministerioId, membroId) {
    const response = await apiClient.post(`/ministerios/${ministerioId}/membros/${membroId}`);
    return response.data;
  },

  // Remover membro do ministério
  async removerMembro(ministerioId, membroId) {
    const response = await apiClient.delete(`/ministerios/${ministerioId}/membros/${membroId}`);
    return response.data;
  },

  // Listar membros do ministério
  async findMembros(ministerioId) {
    const response = await apiClient.get(`/ministerios/${ministerioId}/membros`);
    return response.data;
  },
};

export default ministerioService;
