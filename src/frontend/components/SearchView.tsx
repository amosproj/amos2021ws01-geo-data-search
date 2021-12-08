import React, { FormEvent, useState } from "react";
import osmApiClient from "@lib/api-client";
import SearchInput from "./SearchInput";
import ErrorMessage from "./ErrorMessage";
import {
  SearchQueryResponse,
  SearchResult,
  SearchError,
} from "@lib/types/search";
import SearchListResult from "./SearchListResult";
import { isDevelopment } from "@lib/config";
import { SearchIcon } from "@heroicons/react/outline";
import Spinner from "./Spinner";

const SearchView = () => {
  const [searchValue, setSearchValue] = useState("");
  const [errorData, setErrorData] = useState<SearchError | null>(null);
  const [results, setResults] = useState<SearchResult[] | null>(null);
  const [loading, setLoading] = useState(false);

  const getSearchSuggestions = async (e: FormEvent) => {
    e.preventDefault();

    try {
      setLoading(true);
      setErrorData(null);
      setResults(null);

      const response = await osmApiClient("/user_query", {
        body: {
          query: searchValue,
        },
      });

      if (response.ok) {
        const { result, error }: SearchQueryResponse = await response.json();
        console.log("Search result", result);

        if (error) {
          setErrorData(error);
        } else if (result) {
          setResults(result);
        } else {
          setErrorData({
            type: "system",
            message: "Got empty response from the API",
          });
        }
      } else {
        setErrorData({
          type: "client",
          message: "Looks like the server is down",
          trace: new Error(`${response.status} ${response.statusText}`).stack,
        });
      }
    } catch (err) {
      console.error("Search error", err);
      setErrorData({
        type: "client",
        message: "Something went wrong",
        trace: new Error().stack,
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="my-4">
      <form onSubmit={getSearchSuggestions}>
        <SearchInput
          placeholder="Search"
          value={searchValue}
          onChange={setSearchValue}
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

        {errorData?.message && (
          <ErrorMessage message={`Error: ${errorData.message}`} />
        )}
        {isDevelopment && errorData?.trace && (
          <ErrorMessage message={`Trace: ${errorData.trace}`} />
        )}
      </form>
    </div>
  );
};

export default SearchView;
