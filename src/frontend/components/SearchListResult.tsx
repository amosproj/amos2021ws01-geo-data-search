import { SearchResult } from '@lib/types/search';
import MarkerIcon from '@assets/svg/marker.svg';
import MarkerRouteIcon from '@assets/svg/marker-route.svg';
import { getSearchResultDescription } from '@lib/search';
import { cc } from '@lib/common';

type Props = {
  result: SearchResult;
  onClick?: (result: SearchResult) => void;
  isRoutePart?: boolean;
};

const SearchListResult = ({ result, onClick, isRoutePart }: Props) => {
  const handleResultClick = () => {
    onClick?.(result);
  };

  return (
    <li
      aria-label="Select search result"
      className={cc([
        'p-2 flex items-center hover:bg-[#E6F2F8] cursor-pointer',
        isRoutePart
          ? 'first:rounded-b-none last:rounded-t-none first:rounded-t last:rounded-b'
          : 'rounded',
      ])}
      onClick={handleResultClick}
      role="button"
    >
      {isRoutePart && result.type !== 'Finish' ? (
        <MarkerRouteIcon className="h-3 w-3 mr-3 flex-shrink-0 z-10" />
      ) : (
        <MarkerIcon className="h-5 mr-3 flex-shrink-0" />
      )}
      <div className="overflow-hidden">
        <h3 className="font-bold truncate">{result.name}</h3>
        <p className="text-sm truncate">
          <span className="capitalize">{result.type}</span> ·{' '}
          <span className="text-gray-500">{getSearchResultDescription(result)}</span>
        </p>
      </div>
    </li>
  );
};

export default SearchListResult;
