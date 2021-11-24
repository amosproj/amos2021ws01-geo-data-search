import { SearchResult } from '@lib/types/search';

type Props = {
  result: SearchResult;
};

const SearchListResult = ({ result }: Props) => {
  return (
    <li className="p-2 border-b-2 last:border-0">
      <h3 className="font-bold">{result.name}</h3>
      <p><span>{result.type}</span> · <span className='text-gray-500'>{result.lat}, {result.lon}</span></p>
    </li>
  );
};

export default SearchListResult;
