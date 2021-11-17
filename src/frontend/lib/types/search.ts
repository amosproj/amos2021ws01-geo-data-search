export type SearchError = {
  type: string;
  message: string;
  trace: string;
};

export type SearchResult = {
  type: string;
  id: number;
  lat: string;
  lon: string;
  name: string;
};

export type SearchQueryResponse = {
  result?: SearchResult[];
  error?: SearchError;
};
