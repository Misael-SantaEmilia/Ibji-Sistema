import apiClient from './apiClient';

const pedidoOracaoService = {
  // Listar todos com paginação
  async findAll(params = {}) {
    const response = await apiClient.get('/pedidos-oracao', { params });
    return response.data;
  },

  // Listar pedidos pendentes
  async findPendentes() {
    const response = await apiClient.get('/pedidos-oracao/pendentes');
    return response.data;
  },

  // Listar pedidos aprovados
  async findAprovados() {
    const response = await apiClient.get('/pedidos-oracao/aprovados');
    return response.data;
  },

  // Contar pedidos pendentes
  async countPendentes() {
    const response = await apiClient.get('/pedidos-oracao/count-pendentes');
    return response.data;
  },

  // Buscar por ID
  async findById(id) {
    const response = await apiClient.get(`/pedidos-oracao/${id}`);
    return response.data;
  },

  // Criar pedido
  async create(data) {
    const response = await apiClient.post('/pedidos-oracao', data);
    return response.data;
  },

  // Aprovar pedido
  async aprovar(id, aprovadoPorId) {
    const response = await apiClient.patch(`/pedidos-oracao/${id}/aprovar`, null, {
      params: { aprovadoPorId },
    });
    return response.data;
  },

  // Reprovar (deletar) pedido
  async reprovar(id) {
    await apiClient.delete(`/pedidos-oracao/${id}`);
  },
};

export default pedidoOracaoService;
