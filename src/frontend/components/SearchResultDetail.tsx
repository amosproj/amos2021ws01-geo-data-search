import { ChevronLeftIcon } from '@heroicons/react/outline';
import { getSearchResultDescription } from '@lib/search';
import { SearchResult } from '@lib/types/search';

type Props = { result: SearchResult; onBackClick: () => void };

const SearchResultDetail = ({ result, onBackClick }: Props) => {
  return (
    <div className='sm:mt-4'>
      <div className="flex">
        <button className="pr-2 py-2" onClick={onBackClick}>
          <ChevronLeftIcon className="h-6 w-6" />
        </button>
        <div>
          <h2 className="font-bold text-2xl">{result.name}</h2>
          <p className="text-sm capitalize">{result.type}</p>
        </div>
      </div>
      <h3 className="font-bold mt-4 text-sm">Details</h3>
      <p>{getSearchResultDescription(result)}</p>
    </div>
  );
};

export default SearchResultDetail;