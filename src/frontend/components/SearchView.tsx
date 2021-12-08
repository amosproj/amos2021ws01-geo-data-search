import React, { FormEvent, useRef, useState } from 'react';
import apiClient, { CancelablePromise } from '@lib/api-client';
import SearchInput from './SearchInput';
import ErrorMessage from './ErrorMessage';
import { SearchQueryResponse, SearchError } from '@lib/types/search';
import SearchListResult from './SearchListResult';
import { isDevelopment } from '@lib/config';
import { useAtom } from 'jotai';
import { searchResultsAtom } from '@lib/store';

const SearchView = () => {
  const [searchValue, setSearchValue] = useState('');
  const [errorData, setErrorData] = useState<SearchError | null>(null);
  const [results, setResults] = useAtom(searchResultsAtom)
  const [loading, setLoading] = useState(false);
  const searchQueryPromise = useRef<CancelablePromise<any> | null>(null);

  const getSearchSuggestions = async (e: FormEvent) => {
    e.preventDefault();

    // Wait until the previous search query is finished
    // Normally we should never land here, but just in case
    if (loading) return;

    try {
      setLoading(true);
      setErrorData(null);
      setResults(null);

      // Search request promise
      const promise = apiClient('/user_query', {
        body: {
          query: searchValue,
        },
      });
      // Save the promise to cancel it if the user wants to
      searchQueryPromise.current = promise;

      // Wait for the search request to resolve
      const response = await promise;

      if (response.ok) {
        const { result, error }: SearchQueryResponse = await response.json();
        console.log('Search result', result);

        if (error) {
          setErrorData(error);
        } else if (result) {
          setResults(result);
        } else {
          setErrorData({
            type: 'system',
            message: 'Got empty response from the API',
          });
        }
      } else {
        setErrorData({
          type: 'client',
          message: 'Looks like the server is down',
          trace: new Error(`${response.status} ${response.statusText}`).stack,
        });
      }
    } catch (err) {
      // @ts-ignore
      if (err.name === 'AbortError') {
        // User manually cancelled the search request
        console.log('Request manually canceled by the user');
        return;
      }
      console.error('Search error', err);
      setErrorData({
        type: 'client',
        message: 'Something went wrong',
        trace: new Error().stack,
      });
    } finally {
      setLoading(false);
      searchQueryPromise.current = null;
    }
  };

  const onCancelSearchRequest = () => {
    // If the search request is processing, cancel it
    if (loading && searchQueryPromise.current && searchQueryPromise.current.cancel) {
      searchQueryPromise.current.cancel();
    }
  };

  return (
    <div className="my-4">
      <form onSubmit={getSearchSuggestions}>
        <SearchInput
          placeholder="Search"
          value={searchValue}
          onChange={setSearchValue}
          onCancelSearchRequest={onCancelSearchRequest}
          loading={loading}
        />

        {results &&
          (results.length > 0 ? (
            <ul className="mt-8">
              {results.map((result) => (
                <SearchListResult key={result.id} result={result} />
              ))}
            </ul>
          ) : (
            <p className="mb-2">No results found</p>
          ))}

        {errorData?.message && <ErrorMessage message={`Error: ${errorData.message}`} />}
        {isDevelopment && errorData?.trace && (
          <ErrorMessage message={`Trace: ${errorData.trace}`} />
        )}
      </form>
    </div>
  );
};

export default SearchView;
