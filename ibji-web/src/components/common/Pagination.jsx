import React from 'react';

const Pagination = ({ page, totalPages, onPageChange }) => {
  const pages = [];

  for (let i = 0; i < totalPages; i++) {
    pages.push(
      <button
        key={i}
        className={`pagination-btn ${i === page ? 'active' : ''}`}
        onClick={() => onPageChange(i)}
      >
        {i + 1}
      </button>
    );
  }

  return (
    <div className="pagination">
      <button
        className="pagination-btn"
        onClick={() => onPageChange(page - 1)}
        disabled={page === 0}
      >
        Anterior
      </button>
      {pages}
      <button
        className="pagination-btn"
        onClick={() => onPageChange(page + 1)}
        disabled={page >= totalPages - 1}
      >
        Próximo
      </button>
    </div>
  );
};

export default Pagination;
