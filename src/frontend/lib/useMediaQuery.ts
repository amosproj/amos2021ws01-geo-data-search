import { useEffect, useState } from 'react';

type Options = {
  defaultMatches?: boolean;
  noSsr?: boolean;
};

export default function useMediaQuery(queryInput: string, options: Options = {}) {
  const query = queryInput.replace(/^@media( ?)/m, '');
  const supportMatchMedia =
    typeof window !== 'undefined' && typeof window.matchMedia !== 'undefined';

  const { defaultMatches = false, noSsr = false } = options;
  const matchMedia = supportMatchMedia ? window.matchMedia : null;

  const [match, setMatch] = useState(() => {
    if (noSsr && matchMedia !== null) {
      return matchMedia(query).matches;
    }

    // Once the component is mounted, we rely on the
    // event listeners to return the correct matches value.
    return defaultMatches;
  });

  useEffect(() => {
    if (matchMedia === null) {
      return undefined;
    }

    const queryList = matchMedia(query);
    const updateMatch = () => {
      setMatch(queryList.matches);
    };
    updateMatch();

    queryList.addEventListener('change', updateMatch);

    return () => {
      queryList.removeEventListener('change', updateMatch);
    };
  }, [query, matchMedia, supportMatchMedia]);

  return match;
}
