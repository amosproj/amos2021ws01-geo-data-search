import React, { FormEvent, useRef, useState } from 'react';
import Image from 'next/image';
import apiClient, { CancelablePromise } from '@lib/api-client';
import SearchInput from './SearchInput';
import ErrorMessage from './ErrorMessage';
import { SearchQueryResponse, SearchError } from '@lib/types/search';
import { isDevelopment } from '@lib/config';
import { useAtom } from 'jotai';
import { currentSearchResultAtom, searchResultsAtom } from '@lib/store';
import SearchResults from './SearchResults';
import SearchResultDetail from './SearchResultDetail';
import SearchTips from './SearchTips';

const SearchView = () => {
  const [searchValue, setSearchValue] = useState('');
  const [errorData, setErrorData] = useState<SearchError | null>(null);
  const [results, setResults] = useAtom(searchResultsAtom);
  const [currentSearchResult, setCurrentSearchResult] = useAtom(currentSearchResultAtom);
  const [loading, setLoading] = useState(false);
  const searchQueryPromise = useRef<CancelablePromise<any> | null>(null);

  const onFormSubmit = (e: FormEvent) => {
    e && e.preventDefault();

    getSearchSuggestions(searchValue);
  };

  const getSearchSuggestions = async (query: string) => {
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
          query,
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
          if (!result[0].lon && result[0].lat) { 
            result[0].lon = result[0].lat.split(',')[1]
            result[0].lat = result[0].lat.split(',')[0]
          }
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
          message: "Looks like the server is down or there's an internal server error",
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

      if (currentSearchResult) {
        setCurrentSearchResult(null);
      }
    }
  };

  const onCancelSearchRequest = () => {
    // If the search request is processing, cancel it
    if (loading && searchQueryPromise.current && searchQueryPromise.current.cancel) {
      searchQueryPromise.current.cancel();
    }
  };

  const onCloseSearchResultDetail = () => {
    setCurrentSearchResult(null);
  };

  const onSearchTipSelected = (searchTerm: string) => {
    setSearchValue(searchTerm);
    getSearchSuggestions(searchTerm);
  };

  return (
    <div className="my-4">
      <form onSubmit={onFormSubmit}>
        <div className="flex items-center w-full">
          <div className="h-20 w-20 mr-2 sm:hidden">
            <Image
              className="sm:hidden"
              priority
              layout="responsive"
              width="150"
              height="142"
              src="/images/logo.png"
              alt="Geo Data Search Logo"
            />
          </div>

          <SearchInput
            placeholder="Search"
            value={searchValue}
            onChange={setSearchValue}
            onCancelSearchRequest={onCancelSearchRequest}
            loading={loading}
          />
        </div>
      </form>

      {!currentSearchResult && <SearchTips onSearchTipSelected={onSearchTipSelected} />}

      {currentSearchResult ? (
        <SearchResultDetail onBackClick={onCloseSearchResultDetail} result={currentSearchResult} />
      ) : (
        <SearchResults results={results} />
      )}

      {errorData?.message && <ErrorMessage message={`Error: ${errorData.message}`} />}
      {isDevelopment && errorData?.trace && <ErrorMessage message={`Trace: ${errorData.trace}`} />}
    </div>
  );
};

export default SearchView;
