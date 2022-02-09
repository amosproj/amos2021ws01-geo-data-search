import { SearchResult } from '@lib/types/search';
import { atom } from 'jotai';

export const searchResultsAtom = atom<SearchResult[] | null>(null);

export const currentSearchResultAtom = atom<SearchResult | null>(null);

export const kmlFileAtom = atom<string | null>(null);
