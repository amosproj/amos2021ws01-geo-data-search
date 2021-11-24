import { SearchResult } from './types/search';

export const isDevelopment = process.env.ENVIRONMENT === 'development';
export const FRONTEND_VERSION = '05';

export const sampleSearchResults: SearchResult[] = [
  {
    type: 'river',
    id: 1,
    lat: '38.8951',
    lon: '-77.0364',
    name: 'Spree',
  },
  {
    type: 'mountain',
    id: 2,
    lat: '99.9551',
    lon: '11.1432',
    name: 'Brocken',
  },
  {
    type: 'river',
    id: 3,
    lat: '12.1233',
    lon: '57.0123',
    name: 'Elbe',
  },
];
