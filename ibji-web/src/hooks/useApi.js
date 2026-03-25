import { useState, useEffect, useCallback } from 'react';
import apiClient from '../api/apiClient';

export const useApi = (url, options = {}) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const { immediate = true, params = {} } = options;

  const execute = useCallback(async (customUrl = url) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await apiClient.get(customUrl, { params });
      setData(response.data);
      return response.data;
    } catch (err) {
      setError(err.response?.data || err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [url, params]);

  useEffect(() => {
    if (immediate && url) {
      execute();
    }
  }, [immediate, url, execute]);

  const refetch = useCallback(() => {
    return execute();
  }, [execute]);

  return { data, loading, error, execute, refetch };
};

export const usePagination = (initialPage = 0, initialSize = 20) => {
  const [page, setPage] = useState(initialPage);
  const [size, setSize] = useState(initialSize);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const updatePagination = (response) => {
    setTotalPages(response.totalPages || 0);
    setTotalElements(response.totalElements || 0);
  };

  const nextPage = () => {
    if (page < totalPages - 1) {
      setPage((prev) => prev + 1);
    }
  };

  const prevPage = () => {
    if (page > 0) {
      setPage((prev) => prev - 1);
    }
  };

  const goToPage = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };

  const resetPagination = () => {
    setPage(initialPage);
    setSize(initialSize);
  };

  return {
    page,
    size,
    totalPages,
    totalElements,
    setPage,
    setSize,
    updatePagination,
    nextPage,
    prevPage,
    goToPage,
    resetPagination,
  };
};

export default useApi;