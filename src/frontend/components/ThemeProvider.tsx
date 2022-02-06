import { useEffect, useState, createContext, useContext } from 'react';

export type Theme = 'light' | 'dark';

export const ThemeContext = createContext<
  | {
      activeTheme: Theme;
      toggleTheme: () => void;
    }
  | undefined
>(undefined);

const ThemeProvider = ({ children }: { children: React.ReactNode }) => {
  const [activeTheme, setActiveTheme] = useState<Theme>('light');

  useEffect(() => {
    const initialTheme = document.documentElement.classList.contains('dark') ? 'dark' : 'light';

    setActiveTheme(initialTheme);
  }, []);

  const toggleTheme = () => {
    setActiveTheme(activeTheme === 'light' ? 'dark' : 'light');
  };

  useEffect(() => {
    if (activeTheme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
    window.localStorage.setItem('theme', activeTheme);
  }, [activeTheme]);

  useEffect(() => {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: light)');

    const handleChange = () => {
      setActiveTheme(mediaQuery.matches ? 'light' : 'dark');
    };

    mediaQuery.addEventListener('change', handleChange);

    return () => mediaQuery.removeEventListener('change', handleChange);
  }, []);

  return (
    <ThemeContext.Provider
      value={{
        activeTheme,
        toggleTheme,
      }}
    >
      {children}
    </ThemeContext.Provider>
  );
};

export function useTheme() {
  const context = useContext(ThemeContext);
  if (context === undefined) {
    throw new Error('useTheme must be used within a ThemeProvider');
  }
  return context;
}

export default ThemeProvider;
