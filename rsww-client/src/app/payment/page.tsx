"use client";
import { pay, reserveTrip } from "@/lib/api/reserve-api";
import {
  setReservationId,
  setReservationStatus,
} from "@/lib/redux/reducers/bookingReducer";
import { RootState } from "@/lib/redux/store";
import { Button, Card } from "antd";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

export default function Payment() {
  const dispatch = useDispatch();
  const router = useRouter();
  const reservationStatus = useSelector(
    (state: RootState) => state.booking.reservationStatus
  );
  const tripReservationId = useSelector(
    (state: RootState) => state.booking.reservationId
  );
  const selectedOutBoundFlight = useSelector(
    (state: RootState) => state.transports.selectedFromtransport
  );
  const selectedReturnFlight = useSelector(
    (state: RootState) => state.transports.selectedTotransport
  );
  const selectedHotel = useSelector(
    (state: RootState) => state.hotels.selectedHotel
  );
  const numberOfAdults = useSelector(
    (state: RootState) => state.booking.adults
  );
  const numberOfChildren = useSelector(
    (state: RootState) => state.booking.children
  );
  const numberOfInfants = useSelector(
    (state: RootState) => state.booking.infants
  );
  const numberOfGuests = numberOfAdults + numberOfChildren + numberOfInfants;
  const [isLoading, setIsLoading] = useState(false);
  console.log({
    selectedOutBoundFlight,
    selectedReturnFlight,
    selectedHotel,
  });

  const lengthOfStay =
    Math.ceil(
      (selectedReturnFlight?.departureDate?.getTime() -
        selectedOutBoundFlight?.departureDate?.getTime()) /
        (1000 * 60 * 60 * 24)
    ) ?? 5;

  useEffect(() => {
    if (reservationStatus === "PAID") {
      setIsLoading(false);
      router.push("/confirmation"); // Navigate to confirmation screen
    }
  }, [reservationStatus, router]);

  const formatDateToDDMMYY = (date) => {
    const d = new Date(date);
    const day = d.getDate().toString().padStart(2, "0");
    const month = (d.getMonth() + 1).toString().padStart(2, "0"); // JavaScript months are 0-based.
    const year = d.getFullYear().toString().slice(-2); // Get the last two digits of the year.

    return `${day}/${month}/${year}`;
  };

  return (
    <div>
      <h1>Payment</h1>
      <Card title="Summary" style={{ width: 300 }}>
        <div>
          Outbound Flight:{" "}
          {selectedOutBoundFlight?.departureDate?.toDateString() ?? "-"}
        </div>
        <div>
          Return Flight:{" "}
          {selectedReturnFlight?.departureDate?.toDateString() ?? "-"}
        </div>

        <div>Hotel: {selectedHotel?.name}</div>
        <div>Hotel Price: {selectedHotel?.minPrice ?? 100 * lengthOfStay}</div>
        <div>
          Total Price:{" "}
          {selectedHotel?.minPrice ??
            100 * lengthOfStay +
              numberOfGuests * selectedOutBoundFlight?.basePrice ??
            300 + numberOfGuests * selectedReturnFlight?.basePrice ??
            400}
        </div>
      </Card>
      <Card title="Payment" style={{ width: 300 }}>
        <div>
          Your reservation is created for 180 seconds. Reservation id:{" "}
          {tripReservationId}
        </div>
        <div>Please pay within 180 seconds to confirm your reservation.</div>
      </Card>
      <Button
        type="primary"
        loading={isLoading}
        disabled={isLoading}
        size="large"
        style={{
          backgroundColor: "green",
          borderColor: "green",
          marginTop: "20px",
        }}
        onClick={async () => {
          setIsLoading(true);
          try {
            const response = await reserveTrip(
              selectedHotel.id,
              selectedOutBoundFlight.id,
              selectedReturnFlight.id,
              1,
              formatDateToDDMMYY(selectedOutBoundFlight.departureDate),
              formatDateToDDMMYY(selectedReturnFlight.departureDate),
              numberOfAdults,
              numberOfChildren,
              numberOfInfants
            );
            dispatch(setReservationId(response.reservationId));
            dispatch(setReservationStatus("WAITING_FOR_PAYMENT"));
          } catch (e) {
            console.log(e);
          } finally {
            setIsLoading(false);
          }
        }}
      >
        Reserve
      </Button>
      {true && (
        <Button
          type="primary"
          loading={isLoading}
          disabled={isLoading}
          size="large"
          style={{
            backgroundColor: "green",
            borderColor: "green",
            marginTop: "20px",
          }}
          onClick={async () => {
            setIsLoading(true);
            try {
              const response = await pay(tripReservationId);
            } catch (e) {
              console.log(e);
            } finally {
              setIsLoading(false);
            }
          }}
        >
          Pay
        </Button>
      )}
    </div>
  );
}
