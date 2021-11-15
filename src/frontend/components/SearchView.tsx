import { FormEvent, useState } from 'react';
import apiClient from './lib/api-client';
import SearchInput from './SearchInput';

const SearchView = () => {
  const [searchValue, setSearchValue] = useState('');

  const getSearchSuggestions = async (e: FormEvent) => {
    e.preventDefault();

    try {
      const response = await apiClient('/user_query', {
        body: {
          query: searchValue,
        },
      });

      const data = await response.json();
      console.log(data);
    } catch (err) {
      console.error('Search error', err);
    }
  };

  return (
    <div className="my-8">
      <form className="flex" onClick={getSearchSuggestions}>
        <SearchInput placeholder="Search" value={searchValue} onChange={setSearchValue} />
        <button className="bg-blue-500 text-white rounded px-5 ml-2" type="submit">
          Submit
        </button>
      </form>
    </div>
  );
};

export default SearchView;
