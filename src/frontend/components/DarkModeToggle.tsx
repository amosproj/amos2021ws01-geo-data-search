import { cc } from '@lib/common';
import { useTheme } from './ThemeProvider';

const DarkModeToggle = () => {
  const { activeTheme, toggleTheme } = useTheme();

  return (
    <button
      className={cc([
        'relative flex items-center justify-around text-base leading-none w-16 h-8 p-2 rounded-full',
        'dark:bg-gray-200 dark:text-black bg-[#232F34] text-white',
      ])}
      aria-label={`Toggle theme`}
      title={`Toggle theme`}
      type="button"
      onClick={toggleTheme}
    >
      <span
        className={cc([
          'absolute top-1 left-1 rounded-full bg-white dark:bg-[#232F34] w-6 h-6 transition-transform',
          activeTheme === 'dark' && 'translate-x-8',
        ])}
      />

      {activeTheme === 'light' ? (
        <span className="ml-auto">🌙</span>
      ) : (
        <span className="mr-auto">☀️</span>
      )}
    </button>
  );
};

export default DarkModeToggle;
