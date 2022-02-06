import { useAtom } from 'jotai';
import { currentSearchResultAtom } from '@lib/store';
import { SearchResult } from '@lib/types/search';
import SearchListResult from './SearchListResult';
import { useMemo } from 'react';

const SearchResults = ({ results }: { results: SearchResult[] | null }) => {
  const [, setCurrentSearchResult] = useAtom(currentSearchResultAtom);

  const isRoute = useMemo(() => {
    return results ? results.length >= 2 && results.some((i) => !!i.polyline) : false;
  }, [results]);

  const renderSearchResults = () => {
    return (
      results &&
      results.map((result, index) => (
        <SearchListResult
          key={`${result.id}${index}`}
          result={result}
          onClick={setCurrentSearchResult}
          isRoutePart={isRoute}
        />
      ))
    );
  };

  if (!results) {
    return null;
  }

  if (results.length === 0) {
    return <p className="mb-2">No results found</p>;
  }

  if (isRoute) {
    const start = results.find((i) => i.type === 'Start');
    const finish = results.find((i) => i.type === 'Finish');

    return (
      <div className="">
        <h2 className="text-lg mt-2 dark:text-gray-300">
          Route from <span className="font-bold">{start?.name}</span> to{' '}
          <span className="font-bold">{finish?.name}</span>
        </h2>

        <ul aria-label="Search results" className="sm:mt-2 bg-white dark:bg-transparent dark:border dark:border-gray-700 rounded results--route">
          {renderSearchResults()}
        </ul>
      </div>
    );
  }

  // Regular search results
  return (
    <ul aria-label="Search results" className="sm:mt-4">
      {renderSearchResults()}
    </ul>
  );
};

export default SearchResults;
