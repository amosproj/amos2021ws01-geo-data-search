import React, { FormEvent, useState } from 'react';
import apiClient from '@lib/api-client';
import SearchInput from './SearchInput';
import ErrorMessage from './ErrorMessage';
import { SearchQueryResponse, SearchResult } from '@lib/types/search';

const SearchView = () => {
  const [searchValue, setSearchValue] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [results, setResults] = useState<SearchResult[] | null>(null);
  const [loading, setLoading] = useState(false);

  const getSearchSuggestions = async (e: FormEvent) => {
    e.preventDefault();

    try {
      setLoading(true);
      const response = await apiClient('/user_query', {
        body: {
          query: searchValue,
        },
      });
      if (response.ok) {
        const { result, error }: SearchQueryResponse = await response.json();

        if (error) {
          setErrorMessage(error.message);
        } else if (result) {
          setResults(result);
        }
      } else {
        setErrorMessage('Looks like the server is down');
      }
    } catch (err) {
      console.error('Search error', err);
      setErrorMessage('Something went wrong, see console log for more info');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="my-8">
      <form onSubmit={getSearchSuggestions}>
        <div className="flex mb-4">
          <SearchInput placeholder="Search" value={searchValue} onChange={setSearchValue} />
          <button className="bg-blue-500 text-white rounded px-5 ml-2" type="submit">
            Submit
          </button>
        </div>
        {results && results.length === 0 && <ErrorMessage message={'No results found'} />}
        {errorMessage && <ErrorMessage message={errorMessage} />}
      </form>
    </div>
  );
};

export default SearchView;
