import Head from 'next/head';
import React, { useState } from 'react';
import AboutModal from './AboutModal';
import Modal from './Modal';

type LayoutProps = {
  children: React.ReactNode;
};

const Layout = ({ children }: LayoutProps) => {
  const [showAbout, setShowAbout] = useState(false);

  return (
    <>
      <Head>
        <title>Geo Data Search</title>
      </Head>

      <nav>
        <button
          className="absolute top-0 right-0 p-3 underline text-gray-600"
          onClick={() => setShowAbout(true)}
        >
          About
        </button>
      </nav>

      <main className="container max-w-xl w-full mx-auto my-12">{children}</main>

      {showAbout && <AboutModal open={showAbout} onClose={() => setShowAbout(false)} />}
    </>
  );
};

export default Layout;
