import '../styles/globals.css';
import type { AppProps } from 'next/app';
import useDarkMode from 'use-dark-mode';

function MyApp({ Component, pageProps }: AppProps) {
  const darkMode = useDarkMode(true, {
    classNameDark: 'dark',
  });

  console.log('dark mode', darkMode.value);

  return <Component {...pageProps} />;
}

export default MyApp;
