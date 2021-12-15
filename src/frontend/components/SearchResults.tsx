import { useAtom } from 'jotai';
import { currentSearchResultAtom } from '@lib/store';
import { SearchResult } from '@lib/types/search';
import SearchListResult from './SearchListResult';

const SearchResults = ({ results }: { results: SearchResult[] | null }) => {
  const [, setCurrentSearchResult] = useAtom(currentSearchResultAtom);
  
  return (
    <>
      {results &&
        (results.length > 0 ? (
          <ul className="sm:mt-4">
            {results.map((result) => (
              <SearchListResult key={result.id} result={result} onClick={setCurrentSearchResult} />
            ))}
          </ul>
        ) : (
          <p className="mb-2">No results found</p>
        ))}
    </>
  );
};

export default SearchResults;
