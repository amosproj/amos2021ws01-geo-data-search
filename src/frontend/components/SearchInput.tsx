import React from 'react';
import Spinner from './Spinner';
import { SearchIcon, XIcon } from '@heroicons/react/outline';
import { cc } from '@lib/common';

type Props = {
  onChange: (value: string) => void;
  onCancelSearchRequest: () => void;
  value: string;
  placeholder?: string;
  loading: boolean;
};

const SearchInput = ({ value, onChange, onCancelSearchRequest, placeholder, loading }: Props) => {
  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange?.(e.target.value);
  };

  const onClearSearchTerm = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    onChange?.('');
    if (loading) {
      onCancelSearchRequest?.();
    }
  };

  const searchTermEmpty = value.trim().length === 0;

  return (
    <div className="flex mb-4 rounded w-full border-2 border-solid border-[#DAE5EA]">
      <div className={cc(['relative w-full'])}>
        <input
          className="border-solid rounded-l p-2 w-full focus:outline-none"
          name="searchValue"
          type="text"
          placeholder={placeholder}
          value={value}
          onChange={onInputChange}
        />
        <button
          disabled={!value}
          type="button"
          className="px-2 text-black disabled:text-gray-400 disabled:cursor-default absolute top-0 right-0 h-full"
          onClick={onClearSearchTerm}
        >
          <XIcon className="h-5" />
        </button>
      </div>
      <button
        className="bg-red-500 text-white rounded-r px-3 disabled:opacity-70 disabled:cursor-default"
        type="submit"
        disabled={searchTermEmpty || loading}
      >
        {loading ? (
          <Spinner className="text-white h-5" />
        ) : (
          <SearchIcon className="text-white h-5" />
        )}
      </button>
    </div>
  );
};

export default SearchInput;
