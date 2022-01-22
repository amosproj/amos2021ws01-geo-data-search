export type SearchError = {
  type: string;
  message: string;
  trace?: string;
};

export type SearchResult = {
  type: string;
  id: number;
  lat: string | null;
  lon: string | null;
  name: string;
  tags?: {
    'addr:city': string;
    'addr:country': string;
    'addr:housenumber': string;
    'addr:postcode': string;
    'addr:street': string;
    'addr:suburb': string;
    amenity: string;
    name: string;
  };
  polyline: string;
};

export type SearchQueryResponse = {
  result?: SearchResult[];
  error?: SearchError;
};
