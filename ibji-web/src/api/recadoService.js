import apiClient from './apiClient';

const recadoService = {
  // Listar com paginação
  async findAll(params = {}) {
    const response = await apiClient.get('/recados', { params });
    return response.data;
  },

  // Recados visíveis para o usuário
  async findVisiveis(ministerioId = null) {
    const response = await apiClient.get('/recados/visiveis', {
      params: ministerioId ? { ministerioId } : {},
    });
    return response.data;
  },

  // Recados da igreja (público alvo = IGREJA)
  async findRecadosIgreja() {
    const response = await apiClient.get('/recados/igreja');
    return response.data;
  },

  // Buscar por ID
  async findById(id) {
    const response = await apiClient.get(`/recados/${id}`);
    return response.data;
  },

  // Criar recado
  async create(data, criadoPorId) {
    const response = await apiClient.post('/recados', data, {
      params: { criadoPorId },
    });
    return response.data;
  },

  // Atualizar recado
  async update(id, data) {
    const response = await apiClient.put(`/recados/${id}`, data);
    return response.data;
  },

  // Deletar recado
  async delete(id) {
    await apiClient.delete(`/recados/${id}`);
  },
};

export default recadoService;
