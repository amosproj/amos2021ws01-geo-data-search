import type { NextPage } from "next";
import React from "react";
import SearchLayout from "@components/SearchLayout";
import Head from "next/head";

const Home: NextPage = () => {
  return (
    <>
      <Head>
        <title>Geo Data Search</title>
      </Head>
      <SearchLayout />
    </>
  );
};

export default Home;
