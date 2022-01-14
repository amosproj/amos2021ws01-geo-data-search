import SearchView from '@components/SearchView';
import { currentSearchResultAtom, searchResultsAtom } from '@lib/store';
import { SearchResult } from '@lib/types/search';
import { fireEvent, render, screen } from '@testing-library/react';
import { renderHook } from '@testing-library/react-hooks';
import { useAtom } from 'jotai';
import { act } from 'react-dom/test-utils';

const fakeResults: SearchResult[] = [
  {
    type: 'node',
    id: 1,
    lat: '1234',
    lon: '5678',
    name: 'test node 1',
  },
  {
    type: 'node',
    id: 2,
    lat: '3453',
    lon: '3487',
    name: 'test node 2',
  },
];

const setupSearchForm = () => {
  render(<SearchView />);

  const submit = screen.getByLabelText('Search', { selector: 'button' });
  const clear = screen.getByLabelText('Clear search term', { selector: 'button' });
  const input = screen.getByLabelText('Search term', { selector: 'input' }) as HTMLInputElement;

  return {
    submit,
    clear,
    input,
  };
};

describe('SearchView', () => {
  test('search is not possible with an empty input', () => {
    const { submit, input } = setupSearchForm();

    expect(input.value).toBe('');

    expect(submit).toBeDisabled();
  });

  test('search is possible if the search input is non-empty', () => {
    const { submit, input } = setupSearchForm();

    fireEvent.change(input, { target: { value: 'test' } });

    expect(submit).toBeEnabled();
  });

  test('search term can be cleared using the x button', () => {
    const { clear, input } = setupSearchForm();

    fireEvent.change(input, { target: { value: 'test' } });

    expect(input.value).toBe('test');

    fireEvent.click(clear);

    expect(input.value).toBe('');
  });

  test('search results are shown in the list', () => {
    render(<SearchView />);
    const [, setSearchResults] = renderHook(() => useAtom(searchResultsAtom)).result.current;

    act(() => {
      setSearchResults(fakeResults);
    });

    const resultList = screen.getByLabelText('Search results', { selector: 'ul' });

    expect(resultList.children.length).toBe(2);
  });

  test('current search result is set, when a search result is clicked', () => {
    render(<SearchView />);
    const [, setSearchResults] = renderHook(() => useAtom(searchResultsAtom)).result.current;

    act(() => {
      setSearchResults(fakeResults);
    });

    const searchResults = screen.getAllByLabelText('Select search result', { selector: 'li' });

    fireEvent.click(searchResults[0]);

    const [currentSearchResult] = renderHook(() => useAtom(currentSearchResultAtom)).result.current;

    expect(currentSearchResult).toEqual(fakeResults[0]);
  });

  test('current search result is shown', () => {
    render(<SearchView />);
    const [, setCurrentSearchResult] = renderHook(() => useAtom(currentSearchResultAtom)).result
      .current;

    act(() => {
      setCurrentSearchResult(fakeResults[0]);
    });

    expect(screen.getByTestId('current-search-result-name').textContent).toBe(fakeResults[0].name);
  });

  test('current search result can be closed using the back button', async () => {
    render(<SearchView />);
    const {result} = renderHook(() => useAtom(currentSearchResultAtom))
    const [currentSearchResult, setCurrentSearchResult] = result.current;

    act(() => {
      setCurrentSearchResult(fakeResults[0]);
    });

    expect(currentSearchResult).not.toBeNull();

    act(() => {
      fireEvent.click(screen.getByLabelText('Close current search result', { selector: 'button' }));
    })
    
    const [updatedResult] = renderHook(() => useAtom(currentSearchResultAtom)).result.current;

    expect(updatedResult).toBeNull();
  });
});
