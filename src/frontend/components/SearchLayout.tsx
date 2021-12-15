import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import AboutModal from './AboutModal';
import SearchView from './SearchView';
import dynamic from 'next/dynamic';
import { cc } from '@lib/common';
import { motion } from 'framer-motion';
import useMediaQuery from '@lib/useMediaQuery';
import { useAtom } from 'jotai';
import { currentSearchResultAtom } from '@lib/store';

const MapViewNoSSR = dynamic(() => import('./MapView'), { ssr: false });

const SearchLayout = () => {
  const [currentSearchResult] = useAtom(currentSearchResultAtom);
  const [showAbout, setShowAbout] = useState(false);
  const [expanded, setExpanded] = useState(false);
  const isDesktop = useMediaQuery('(min-width: 640px)');
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
            currentSearchResult ? 'bg-white' : 'bg-[#DAE5EA]',
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
            <button className="absolute inset-0 z-10 w-full h-full" onClick={handleFullscreen} />
          )}

          <div className="flex flex-col p-5 h-full">
            {isMobile && (
              <button
                className="flex items-center text-center mx-auto pt-1"
                onClick={toggleFullScreen}
              >
                <span className="bg-black w-16 h-[2px]" />
              </button>
            )}

            <div className="justify-center hidden sm:flex">
              <Image
                priority
                layout="fixed"
                width="150"
                height="142"
                src="/images/logo.png"
                alt="Geo Data Search Logo"
              />
            </div>

            <SearchView />

            {(expanded || isDesktop) && (
              <nav className="mt-auto pb-2 text-center">
                <button className="underline text-gray-600" onClick={() => setShowAbout(true)}>
                  About
                </button>
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
