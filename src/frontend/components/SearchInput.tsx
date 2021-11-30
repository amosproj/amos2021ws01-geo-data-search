import React, { ChangeEvent } from "react";
import Spinner from "./Spinner";
import { SearchIcon } from "@heroicons/react/outline";

type Props = {
  onChange: (value: string) => void;
  value: string;
  placeholder?: string;
  loading: boolean;
};

const SearchInput = ({ value, onChange, placeholder, loading }: Props) => {
  const onInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    onChange?.(e.target.value);
  };
  
  const searchTermEmpty = value.trim().length === 0;

  return (
    <div className="flex mb-4 shadow-sm rounded">
      <input
        className="border-solid rounded-l p-2 w-full focus:outline-none"
        name="searchValue"
        type="text"
        placeholder={placeholder}
        value={value}
        onChange={onInputChange}
      />
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
