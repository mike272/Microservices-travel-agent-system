import { RootState } from "@/lib/redux/store";
import { Button, Card } from "antd";
import { useSelector } from "react-redux";

export default function Payment() {
  const selectedOutBoundFlight = useSelector(
    (state: RootState) => state.transports.selectedFromtransport
  );
  const selectedReturnFlight = useSelector(
    (state: RootState) => state.transports.selectedTotransport
  );
  const selectedHotel = useSelector(
    (state: RootState) => state.hotels.selectedHotel
  );
  const numberOfGuests = useSelector(
    (state: RootState) =>
      state.booking.adults + state.booking.children + state.booking.infants
  );
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
        size="large"
        style={{
          backgroundColor: "green",
          borderColor: "green",
          marginTop: "20px",
        }}
      >
        Pay
      </Button>
    </div>
  );
}
