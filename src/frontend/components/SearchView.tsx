import { useEffect, useState } from "react";
import SearchInput from "./SearchInput";

const SearchView = () => {
  const [searchValue, setSearchValue] = useState("");

  return (
    <div className="my-8">
      <SearchInput placeholder="Search" value={searchValue} onChange={setSearchValue} />
    </div>
  );
};

export default SearchView;
