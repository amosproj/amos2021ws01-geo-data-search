import type { NextPage } from "next";
import Head from "next/head";
import SearchView from "../components/SearchView";

const Home: NextPage = () => {
  return (
    <div>
      <Head>
        <title>Geo Data Search</title>
      </Head>

      <main className='container max-w-xl w-full mx-auto my-12'>
        <h1 className='text-3xl font-bold'>Geo Data Search</h1>
        
        <SearchView />
      </main>
    </div>
  );
};

export default Home;
