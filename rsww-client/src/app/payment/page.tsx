import { reserveTrip } from "@/lib/api/reserve-api";
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

  const lengthOfStay = Math.ceil(
    (selectedReturnFlight.departureDate.getTime() -
      selectedOutBoundFlight.departureDate.getTime()) /
      (1000 * 60 * 60 * 24)
  );

  useEffect(() => {
    if (reservationStatus === "PAID") {
      setIsLoading(false);
      router.push("/confirmation"); // Navigate to confirmation screen
    }
  }, [reservationStatus, router]);

  return (
    <div>
      <h1>Payment</h1>
      <Card title="Summary" style={{ width: 300 }}>
        <div>
          Outbound Flight: {selectedOutBoundFlight.departureDate.toDateString()}
        </div>
        <div>
          Return Flight: {selectedReturnFlight.departureDate.toDateString()}
        </div>

        <div>Hotel: {selectedHotel.name}</div>
        <div>Hotel Price: {selectedHotel.minPrice * lengthOfStay}</div>
        <div>
          Total Price:{" "}
          {selectedHotel.minPrice * lengthOfStay +
            numberOfGuests * selectedOutBoundFlight.basePrice +
            numberOfGuests * selectedReturnFlight.basePrice}
        </div>
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
              selectedOutBoundFlight.departureDate.toISOString(),
              selectedReturnFlight.departureDate.toISOString(),
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
        Pay
      </Button>
    </div>
  );
}
