/* eslint-disable @next/next/no-img-element */
"use client";
import {
  setFromTransportation,
  setToTransportation,
} from "@/lib/redux/reducers/transportsReducer";
import { RootState } from "@/lib/redux/store";
import { Button } from "antd";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";

export default function Hotels() {
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
  const transports = useSelector(
    (state: RootState) => state.transports.transports
  ).sort((a, b) => a.fromDate.getTime() - b.fromDate.getTime());

  const departureTransports = transports.filter(
    (transport) => transport.fromLocation === fromLocation
  );
  const returnTransports = transports.filter(
    (transport) => transport.fromLocation === toLocation
  );

  useEffect(() => {
    if (departureFlight && returnFlight) {
      router.push("/hotels");
    }
  }, [departureFlight, returnFlight, router]);

  return (
    <div style={{ display: "flex", justifyContent: "space-between" }}>
      <div>
        <h1>Departure Transports</h1>
        <ul>
          {departureTransports?.map((transport, index) => (
            <li key={transport.id}>
              <div>
                <h2>{transport.name}</h2>
                <p>From: {transport.fromLocation}</p>
                <p>To: {transport.toLocation}</p>
                <p>From Date: {transport.fromDate.toDateString()}</p>
                <p>To Date: {transport.toDate.toDateString()}</p>
                <p>Price: ${transport.price}</p>
                <Button
                  className="bg-blue-500 hover:bg-blue-700 text-black font-bold px-4 rounded"
                  onClick={() => dispatch(setFromTransportation(transport))}
                >
                  Select
                </Button>
              </div>
            </li>
          ))}
        </ul>
      </div>
      <div>
        <h1>Return Transports</h1>
        <ul>
          {returnTransports?.map((transport, index) => (
            <li key={transport.id}>
              <div>
                <h2>{transport.name}</h2>
                <p>From: {transport.fromLocation}</p>
                <p>To: {transport.toLocation}</p>
                <p>From Date: {transport.fromDate.toDateString()}</p>
                <p>To Date: {transport.toDate.toDateString()}</p>
                <p>Price: ${transport.price}</p>
                <Button
                  className="bg-blue-500 hover:bg-blue-700 text-black font-bold px-4 rounded"
                  onClick={() => dispatch(setToTransportation(transport))}
                >
                  Select
                </Button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
