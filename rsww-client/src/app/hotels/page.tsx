"use client";
import { RootState } from "@/lib/redux/store";
import React from "react";
import { useSelector, useDispatch } from "react-redux";

export default function Hotels() {
  const hotels = useSelector((state: RootState) => state.hotels.hotels);
  return (
    <div>
      <h1>Hotels</h1>
      <ul>
        {hotels?.map((hotel) => (
          <li key={hotel.id}>{hotel.name}</li>
        ))}
      </ul>
    </div>
  );
}
