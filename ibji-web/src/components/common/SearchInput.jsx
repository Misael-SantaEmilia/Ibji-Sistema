import React from 'react';
import { FaSearch } from 'react-icons/fa';

const SearchInput = ({ value, onChange, placeholder = 'Buscar...', onClear }) => {
  return (
    <div className="search-input-container">
      <FaSearch className="search-icon" />
      <input
        type="text"
        className="search-input"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
      />
      {value && onClear && (
        <button className="search-clear" onClick={onClear}>
          &times;
        </button>
      )}
    </div>
  );
};

export default SearchInput;