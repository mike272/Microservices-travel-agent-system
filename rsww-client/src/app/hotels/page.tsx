/* eslint-disable @next/next/no-img-element */
"use client";
import { RootState } from "@/lib/redux/store";
import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import hotel1Image from "../../../public/hotel1.png";
import hotel2Image from "../../../public/hotel2.png";
import hotel3Image from "../../../public/hotel3.png";
import { searchHotels } from "@/lib/api/search-api";
import { Button } from "antd";
import { Hotel } from "@/lib/utils/types";
import { setHotels } from "@/lib/redux/reducers/hotelsReducer";

export default function Hotels() {
  const hotels = useSelector((state: RootState) => state.hotels.hotels);
  const bookingPreferences = useSelector((state: RootState) => state.booking);
  const dispatch = useDispatch();
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
    console.log({ response, hotels });
    dispatch(setHotels(response?.hotels));
  }

  useEffect(() => {
    if (hotels.length === 0) {
      fetchHotels();
    }
  }, []);

  function FlightRow({
    hotel,
    onClick,
    index,
  }: {
    hotel: Hotel;
    onClick: () => void;
    index: number;
  }) {
    return (
      <li key={hotel.id}>
        <div className="mb-6 flex flex-row justify-between h-40 align-middle rounded-lg border border-solid border-gray-600">
          <img
            width={200}
            height={160}
            src={
              index % 3 === 0
                ? hotel1Image.src
                : index % 3 === 1
                ? hotel2Image.src
                : hotel3Image.src
            }
            alt="Hotel"
            style={{
              borderRadius: "10px",
              width: "200px",
              height: "160px",
              minWidth: "200px",
              minHeight: "160px",
              marginRight: "15px",
            }}
          />
          <div className="flex flex-col mr-8">
            <div className="font-normal text-xl">{hotel.name}</div>
            <div className="font-normal text-lg">
              {hotel.city}, {hotel.country}
            </div>
          </div>
          <div className="flex flex-col mr-8">
            <div className="font-normal text-lg">{hotel.description}</div>
          </div>
          <div className="flex flex-col mr-8">
            <p>Price per night: ${hotel.minPrice}</p>
            <p>Rooms: {hotel.rooms}</p>
          </div>

          <Button
            className="bg-blue-500 hover:bg-blue-700 text-black font-bold px-4 rounded"
            onClick={onClick}
          >
            Select
          </Button>
        </div>
      </li>
    );
  }

  return (
    <div>
      <h1>Hotels</h1>
      <ul>
        {hotels?.map((hotel, index) => (
          <FlightRow
            key={index}
            hotel={hotel}
            onClick={() => console.log("Selected hotel", hotel)}
            index={index}
          />
        ))}
      </ul>
    </div>
  );
}
