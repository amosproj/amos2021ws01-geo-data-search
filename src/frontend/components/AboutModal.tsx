import { FRONTEND_VERSION } from '@lib/config';
import { fetcher } from '@lib/fetcher';
import React from 'react';
import useSWR from 'swr/immutable';
import Modal from './Modal';

type AboutModalProps = {
  open: boolean;
  onClose: () => void;
};

type VersionResponse = {
  error: any;
  result?: {
    backend: string;
    nlp: string;
  };
};

const VersionItem = ({ name, version }: { name: string; version?: string }) => (
  <li>
    {name}: {version}
  </li>
);

const AboutModal = ({ open, onClose }: AboutModalProps) => {
  const { data } = useSWR<VersionResponse>(`/api/version`, fetcher);

  return (
    <Modal title="About" onClose={onClose} open={open}>
      <p className="font-bold">Versions:</p>

      <ul className="">
        <VersionItem name="Frontend" version={FRONTEND_VERSION} />
        <VersionItem name="Backend" version={data?.result?.backend} />
        <VersionItem name="NLP" version={data?.result?.nlp} />
      </ul>
    </Modal>
  );
};

export default AboutModal;
