import { Dialog } from '@headlessui/react';
import React from 'react';
import { XIcon } from '@heroicons/react/outline';

type Props = {
  onClose: () => void;
  open: boolean;
  children: React.ReactNode;
  title: string;
};

function Modal({ open, onClose, children, title }: Props) {
  return (
    <Dialog open={open} onClose={onClose} className="fixed z-10 inset-0 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen">
        <Dialog.Overlay className="fixed inset-0 bg-black opacity-30" />

        <div className="relative bg-white dark:bg-[#394F58] dark:text-gray-300 rounded-lg max-w-lg w-full mx-auto p-6">
          <button
            className="absolute top-0 right-0 p-2 hover:opacity-50"
            onClick={onClose}
            aria-label="Close modal"
            title="Close modal"
          >
            <XIcon className="h-8 w-8" />
          </button>

          <Dialog.Title className="font-bold text-2xl mb-4">{title}</Dialog.Title>

          {children}
        </div>
      </div>
    </Dialog>
  );
}

export default Modal;
