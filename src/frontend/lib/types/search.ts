export type SearchError = {
  type: string;
  message: string;
  trace?: string;
};

export type SearchResult = {
  type: string;
  id: number;
  lat: string;
  lon: string;
  name: string;
  tags: {
    'addr:city': string;
    'addr:country': string;
    'addr:housenumber': string;
    'addr:postcode': string;
    'addr:street': string;
    'addr:suburb': string;
    amenity: string;
    name: string;
  };
};

export type SearchQueryResponse = {
  result?: SearchResult[];
  error?: SearchError;
};
