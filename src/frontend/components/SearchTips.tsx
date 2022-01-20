import { useState } from 'react';
import Modal from './Modal';
import { AnimatePresence, motion } from 'framer-motion';
import useInterval from '@lib/useInterval';

const searchTermExamples = [
  'Zeige mir Berge in Berlin',
  // 'Zeige mir einen Weg von Essen nach Köln',
  'Wie komme ich von Essaouira nach Ourzazate?',
  'Plane mir eine Route von Berlin nach Paris mit Stromtankstellen',
  'Gibt es hohe Hügel in Garmisch-Partenkirchen',
  'Route von Cannes nach Nizza ohne Mautstraßen',
];

type Props = { onSearchTipSelected: (searchTerm: string) => void };

const SearchTips = ({ onSearchTipSelected }: Props) => {
  const [isOpen, setIsOpen] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(0);

  const onOpen = () => {
    setIsOpen(true);
  };
  const onClose = () => {
    setIsOpen(false);
  };

  useInterval(() => {
    setCurrentIndex((currentIndex + 1) % searchTermExamples.length);
  }, 5000);

  return (
    <>
      <div className="mt-2 mb-4 ml-2">
        <button onClick={onOpen} className="text-gray-500 flex items-center group">
          <span className="mr-2 font-bold text-sm rounded-full border-2 border-gray-500 h-5 w-5 flex items-center justify-center">
            ?
          </span>
          <span className="mr-1">z.B.</span>
          <AnimatePresence>
            <motion.span
              key={currentIndex}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="italic group-hover:underline truncate max-w-[250px]"
            >
              {searchTermExamples[currentIndex]}
            </motion.span>
          </AnimatePresence>
        </button>
      </div>
      {isOpen && (
        <Modal title="Example queries" onClose={onClose} open={isOpen}>
          <ul className="list-disc pl-6 mb-4">
            {searchTermExamples.map((i, index) => (
              <li
                role="button"
                className="hover:underline"
                key={index}
                onClick={(e) => {
                  e.preventDefault();
                  onClose();
                  onSearchTipSelected(i);
                }}
              >
                {i}
              </li>
            ))}
          </ul>
          <a
            target="_blank"
            rel="noopener noreferrer"
            className="underline"
            href="https://github.com/amosproj/amos2021ws01-geo-data-search/wiki/User-Documentation"
          >
            User Documentation
          </a>
        </Modal>
      )}
    </>
  );
};

export default SearchTips;
