import { SearchResult } from "./types/search";

export const getSearchResultDescription = ({ tags, lat, lon }: SearchResult) => {
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