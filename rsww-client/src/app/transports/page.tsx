/* eslint-disable @next/next/no-img-element */
"use client";
import {
  setFromTransportation,
  setToTransportation,
} from "@/lib/redux/reducers/transportsReducer";
import { RootState } from "@/lib/redux/store";
import { compareLocation } from "@/lib/utils";
import { Button } from "antd";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";

export default function Transports() {
  const dispatch = useDispatch();
  const router = useRouter();

  const departureFlight = useSelector(
    (state: RootState) => state.transports.selectedFromtransport
  );
  const returnFlight = useSelector(
    (state: RootState) => state.transports.selectedTotransport
  );

  const fromLocation = useSelector(
    (state: RootState) => state.booking.fromLocation
  );
  const toLocation = useSelector(
    (state: RootState) => state.booking.toLocation
  );
  console.log({
    departureFlight,
    returnFlight,
    fromLocation,
    toLocation,
  });
  const transports = useSelector(
    (state: RootState) => state.transports.transports
  );
  // const sortedTransports = transports.sort(
  //   (a, b) => a.departureDate?.getTime() - b.departureDate?.getTime()
  // );

  const departureTransports = transports.filter((transport) =>
    compareLocation(
      fromLocation,
      transport.departureCity,
      transport.departureCountry
    )
  );
  const returnTransports = transports.filter((transport) =>
    compareLocation(
      toLocation,
      transport.destinationCity,
      transport.departureCountry
    )
  );

  useEffect(() => {
    if (departureFlight && returnFlight) {
      router.push("/hotels");
    }
  }, [departureFlight, returnFlight, router]);

  function FlightRow({ transport, onClick }) {
    return (
      <li key={transport.id}>
        <div className="mb-6 flex flex-row justify-between align-middle rounded-lg border border-solid border-gray-600">
          <div className="flex flex-col mr-8">
            <div className="font-normal text-lg">
              {transport.departureDate.toDateString()}
            </div>
            <div className="font-normal text-lg">
              From: {transport.departureCountry}, {transport.departureCity}
            </div>
          </div>
          <div className="flex flex-col mr-8">
            <div className="font-normal text-lg">
              {transport.departureDate.toDateString()}
            </div>
            <div>
              To: {transport.destinationCountry}, {transport.destinationCity}
            </div>
          </div>
          <div className="flex flex-col mr-8">
            <p>Price: ${transport.basePrice}</p>
            <p>
              Available Places: {transport.availablePlaces}/
              {transport.totalPlaces}
            </p>
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
    <div style={{ display: "flex", justifyContent: "space-between" }}>
      <div>
        <h1>Departure Transports</h1>
        <ul>
          {departureTransports?.map((transport, index) =>
            FlightRow({
              transport,
              onClick: () => dispatch(setFromTransportation(transport)),
            })
          )}
        </ul>
      </div>
      <div>
        <h1>Return Transports</h1>
        <ul>
          {returnTransports?.map((transport, index) => (
            <FlightRow
              key={index}
              transport={transport}
              onClick={() => dispatch(setToTransportation(transport))}
            />
          ))}
        </ul>
      </div>
    </div>
  );
}
