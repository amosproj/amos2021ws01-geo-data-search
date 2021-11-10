import { FormEvent, useState } from "react";
import apiClient from "./lib/api-client";
import SearchInput from "./SearchInput";

const SearchView = () => {
  const [searchValue, setSearchValue] = useState("");

  const getSearchSuggestions = async (e: FormEvent) => {
    e.preventDefault();

    const searchQuery = {
      q: searchValue,
    };

    const queryString = new URLSearchParams(searchQuery).toString();

    try {
      await apiClient(`/search?${queryString}`);
    } catch (err) {
      console.error("Search error", err);
    }
  };

  return (
    <div className="my-8">
      <form className="flex" onClick={getSearchSuggestions}>
        <SearchInput
          placeholder="Search"
          value={searchValue}
          onChange={setSearchValue}
        />
        <button
          className="bg-blue-500 text-white rounded px-5 ml-2"
          type="submit"
        >
          Submit
        </button>
      </form>
    </div>
  );
};

export default SearchView;
