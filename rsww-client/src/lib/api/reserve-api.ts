async function reserveTrip(
  hotelId: number,
  outgoingTransportId: number,
  returnTransportId: number,
  customerId: number,
  fromDate: string,
  toDate: string,
  adults: number,
  children: number,
  infants: number
) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_GATEWAY_ADDRESS}/v1/trips/reserve`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        hotelId,
        outgoingTransportId,
        returnTransportId,
        customerId,
        fromDate,
        toDate,
        adults,
        children,
        infants,
      }),
    }
  );

  if (!response.ok) {
    throw new Error(`API request failed with status ${response.status}`);
  }

  const data = await response.json();
  return data;
}

export { reserveTrip };
