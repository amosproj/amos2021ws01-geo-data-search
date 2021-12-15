import { SearchResult } from '@lib/types/search';
import MarkerIcon from '@assets/svg/marker.svg';

type Props = {
  result: SearchResult;
};

const getSearchResultDescription = ({ tags, lat, lon }: SearchResult) => {
  if (tags) {
    const street = tags['addr:street'];
    const city = tags['addr:city'];
    const postcode = tags['addr:postcode'];
    const housenumber = tags['addr:housenumber'];

    if (street && city && postcode && housenumber) {
      return `${street}, ${housenumber}, ${postcode} ${city}`;
    }

    if (postcode && city) {
      return `${city} ${postcode}`;
    }
  }

  return `${lat}, ${lon}`;
};

const SearchListResult = ({ result }: Props) => {
  return (
    <li className="p-2 flex items-center">
      <MarkerIcon className="h-5 mr-3" />
      <div>
        <h3 className="font-bold">{result.name}</h3>
        <p className="text-sm">
          <span className="capitalize">{result.type}</span> ·{" "}
          <span className="text-gray-500">
            {getSearchResultDescription(result)}
          </span>
        </p>
      </div>
    </li>
  );
};

export default SearchListResult;
