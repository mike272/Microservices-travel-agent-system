/* eslint-disable @next/next/no-img-element */
"use client";
import { RootState } from "@/lib/redux/store";
import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import hotel1Image from "../../../public/hotel1.png";
import hotel2Image from "../../../public/hotel2.png";
import hotel3Image from "../../../public/hotel3.png";
import { searchHotels } from "@/lib/api/search-api";

export default function Hotels() {
  const hotels = useSelector((state: RootState) => state.hotels.hotels);
  const bookingPreferences = useSelector((state: RootState) => state.booking);

  async function fetchHotels() {
    const response = await searchHotels(
      bookingPreferences.toLocation,
      bookingPreferences.fromDate,
      bookingPreferences.toDate,
      {
        adults: bookingPreferences.adults,
        children: bookingPreferences.children,
        infants: bookingPreferences.infants,
      }
    );
  }

  useEffect(() => {
    if (hotels.length === 0) {
      fetchHotels();
    }
  }, []);

  return (
    <div>
      <h1>Hotels</h1>
      <ul>
        {hotels?.map((hotel, index) => (
          <li key={hotel.id}>
            <img
              src={
                index % 3 === 0
                  ? hotel1Image.src
                  : index % 3 === 1
                  ? hotel2Image.src
                  : hotel3Image.src
              }
              alt="Hotel"
              style={{ borderRadius: "10px" }}
            />
            <div>
              <h2>{hotel.name}</h2>
              <p>Stars: {hotel.stars}</p>
              <p>About: {hotel.about}</p>
              <p>Are Kids Allowed: {hotel.areKidsAllowed ? "Yes" : "No"}</p>
              <p>City: {hotel.city}</p>
              <p>City: {hotel.city}</p>
              <p>Rooms: {hotel.rooms}</p>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
