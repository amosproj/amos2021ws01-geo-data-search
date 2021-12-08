import React, { useState } from 'react';
import AboutModal from './AboutModal';
import SearchView from './SearchView';
import dynamic from 'next/dynamic'

const MapViewNoSSR = dynamic(
  () => import('./MapView'),
  { ssr: false }
)

const SearchLayout = () => {
  const [showAbout, setShowAbout] = useState(false);

  return (
    <>
      <div className="flex h-full">
        <div className="bg-[#DAE5EA] p-5 flex flex-col w-full max-w-[400px] overflow-y-auto relative z-20 shadow-3xl">
          <img className="mx-auto" width="150" src="/images/logo.png" alt="Geo Data Search Logo" />

          <SearchView />

          <nav className="mt-auto text-center">
            <button className="underline text-gray-600" onClick={() => setShowAbout(true)}>
              About
            </button>
          </nav>
        </div>

        <div className="w-full z-10 relative">
          <MapViewNoSSR />
        </div>
      </div>

      {showAbout && <AboutModal open={showAbout} onClose={() => setShowAbout(false)} />}
    </>
  );
};

export default SearchLayout;
