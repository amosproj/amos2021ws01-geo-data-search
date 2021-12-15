import { SearchResult } from '@lib/types/search';
import { atom } from 'jotai';

export const searchResultsAtom = atom<SearchResult[] | null>(null);
