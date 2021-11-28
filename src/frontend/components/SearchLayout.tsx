import React, { useState } from "react";
import AboutModal from "./AboutModal";
import SearchView from "./SearchView";

const SearchLayout = () => {
  const [showAbout, setShowAbout] = useState(false);

  return (
    <>
      <div className="flex h-full">
        <div className="bg-[#DAE5EA] p-5 flex flex-col w-full max-w-[400px] shadow-lg">
          <img
            className="mx-auto"
            width="150"
            src="/images/logo.png"
            alt="Geo Data Search Logo"
          />

          <SearchView />

          <nav className='mt-auto text-center'>
            <button
              className="underline text-gray-600"
              onClick={() => setShowAbout(true)}
            >
              About
            </button>
          </nav>
        </div>

        <div className="bg-green-100"></div>
      </div>

      {showAbout && (
        <AboutModal open={showAbout} onClose={() => setShowAbout(false)} />
      )}
    </>
  );
};

export default SearchLayout;
