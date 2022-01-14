import { SearchResult } from '@lib/types/search';
import MarkerIcon from '@assets/svg/marker.svg';
import { getSearchResultDescription } from '@lib/search';

type Props = {
  result: SearchResult;
  onClick?: (result: SearchResult) => void;
};

const SearchListResult = ({ result, onClick }: Props) => {
  const handleResultClick = () => {
    onClick?.(result);
  };

  return (
    <li
      aria-label='Select search result'
      className="p-2 flex items-center hover:bg-[#E6F2F8] rounded cursor-pointer"
      onClick={handleResultClick}
      role="button"
    >
      <MarkerIcon className="h-5 mr-3 flex-shrink-0" />
      <div>
        <h3 className="font-bold">{result.name}</h3>
        <p className="text-sm">
          <span className="capitalize">{result.type}</span> ·{' '}
          <span className="text-gray-500">{getSearchResultDescription(result)}</span>
        </p>
      </div>
    </li>
  );
};

export default SearchListResult;
