"use client";
import { RootState } from "@/lib/redux/store";
import { useSelector } from "react-redux";

export default function Page() {
  const bookingId = useSelector(
    (state: RootState) => state.booking.reservationId
  );
  return <div>Your confirmation {bookingId} is confirmed</div>;
}
