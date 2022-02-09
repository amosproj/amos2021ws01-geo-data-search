import React, { useEffect, useState } from 'react';
import AboutModal from './AboutModal';
import SearchView from './SearchView';
import dynamic from 'next/dynamic';
import { cc } from '@lib/common';
import { motion } from 'framer-motion';
import useMediaQuery from '@lib/useMediaQuery';
import { useAtom } from 'jotai';
import { currentSearchResultAtom, kmlFileAtom } from '@lib/store';
import DarkModeToggle from './DarkModeToggle';
import Logo from './Logo';
import { DocumentDownloadIcon, InformationCircleIcon } from '@heroicons/react/outline';

const MapViewNoSSR = dynamic(() => import('./MapView'), { ssr: false });

const SearchLayout = () => {
  const [currentSearchResult] = useAtom(currentSearchResultAtom);
  const [showAbout, setShowAbout] = useState(false);
  const [expanded, setExpanded] = useState(false);
  const isDesktop = useMediaQuery('(min-width: 640px)', {
    defaultMatches: true,
  });
  const [kmlFile] = useAtom(kmlFileAtom);
  const isMobile = !isDesktop;

  useEffect(() => {
    if (currentSearchResult && isMobile) {
      setExpanded(false);
    }
  }, [currentSearchResult, isMobile]);

  const handleFullscreen = () => {
    if (!expanded) {
      setExpanded(true);
    }
  };

  const toggleFullScreen = () => {
    setExpanded(!expanded);
  };

  return (
    <>
      <div className="flex flex-col sm:flex-row h-full">
        <motion.div
          className={cc([
            'absolute sm:relative bottom-0 sm:bottom-auto transition-colors',
            'w-full sm:max-w-[320px] lg:max-w-[400px]',
            'sm:overflow-y-auto z-20 shadow-3xl',
            isMobile && (expanded ? 'overflow-y-auto' : 'overflow-hidden'),
            currentSearchResult ? 'bg-white dark:bg-[#546A74]' : 'bg-[#DAE5EA] dark:bg-[#232F34]',
          ])}
          animate={
            isMobile
              ? {
                  height: expanded ? '90%' : currentSearchResult ? 300 : 200,
                }
              : { height: '100%' }
          }
          transition={{ type: 'spring', bounce: 0, duration: 0.5 }}
        >
          {isMobile && !expanded && (
            <button
              className="absolute inset-0 z-10 w-full h-full"
              onClick={handleFullscreen}
              aria-label="Open fullscreen"
              title="Open fullscreen"
            />
          )}

          <div className="flex flex-col p-5 h-full">
            {isMobile && (
              <button
                className="flex items-center text-center mx-auto pt-1"
                onClick={toggleFullScreen}
                title="Toggle fullscreen"
                aria-label="Toggle fullscreen"
              >
                <span className="bg-black dark:bg-white w-16 h-[2px]" />
              </button>
            )}

            <div className="justify-center hidden sm:flex">
              <Logo />
            </div>

            <SearchView />

            {(expanded || isDesktop) && (
              <nav className="mt-auto pb-2 text-center flex justify-between">
                <button
                  type="button"
                  className="flex items-center text-gray-600 dark:text-gray-300"
                  onClick={() => setShowAbout(true)}
                >
                  <InformationCircleIcon className="w-6 h-6 mr-1" />
                  About
                </button>

                <a
                  aria-label="Download KML file with results"
                  title="Download KML file with results"
                  className={cc([
                    'flex items-center text-gray-600 dark:text-gray-300',
                    kmlFile === null && 'cursor-not-allowed opacity-50',
                  ])}
                  href={kmlFile ? `/api/kml?fileName=${kmlFile}` : undefined}
                  download
                >
                  <DocumentDownloadIcon className="h-6 w-6 mr-1" />
                  KML
                </a>

                <DarkModeToggle />
              </nav>
            )}
          </div>
        </motion.div>

        <div className="w-full z-10 relative h-full mb-[200px] sm:mb-0">
          <MapViewNoSSR />
        </div>
      </div>

      {showAbout && <AboutModal open={showAbout} onClose={() => setShowAbout(false)} />}
    </>
  );
};

export default SearchLayout;
