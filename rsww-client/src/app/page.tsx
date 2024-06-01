"use client";
import { SearchBar } from "@/lib/components/search-bar/search-bar";
import { TripList } from "@/lib/components/trips-list/trips-list";
import { useEffect, useState } from "react";
import { Button } from "antd";
import { initialize } from "@/lib/api/initialize-api";

export default function Home() {
  const [tripsList, setTripsList] = useState([]);
  const [hotelsList, setHotelsList] = useState([]);

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <Button
        onClick={() => initialize()}
        type="primary"
        style={{ position: "absolute", top: 0, left: 0 }}
      >
        Initialize
      </Button>
      <h3>Witaj na stronie klienckiej projektu RSWW. Dokąd chcesz się udać?</h3>
      <SearchBar />
      <TripList tripsList={tripsList} />
    </main>
  );
}
