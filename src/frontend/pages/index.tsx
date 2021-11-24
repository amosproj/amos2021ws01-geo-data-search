import type { NextPage } from 'next';
import React from 'react';
import SearchView from '@components/SearchView';
import Layout from '@components/Layout';

const Home: NextPage = () => {
  return (
    <Layout>
      <h1 className="text-3xl font-bold">Geo Data Search</h1>

      <SearchView />
    </Layout>
  );
};

export default Home;
