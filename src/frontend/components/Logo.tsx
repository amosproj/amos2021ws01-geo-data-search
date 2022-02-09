import Image from 'next/image';
import { useTheme } from './ThemeProvider';

export default function Logo() {
  const { activeTheme } = useTheme();
  const logoSrc = activeTheme === 'dark' ? '/images/logo-dark.png' : '/images/logo.png';

  return (
    <Image
      priority
      layout="intrinsic"
      width="150"
      height="142"
      src={logoSrc}
      alt="Geo Data Search Logo"
    />
  );
}
