// async function searchTrips(
//   fromLocation: string | null,
//   toLocation: string | null,
//   dateFrom: string | null,
//   dateTo: string | null,
//   passengers: {
//     adults: number;
//     children: number;
//     infants: number;
//   }
// ) {
//   let body = {
//     fromLocation,
//     toLocation,
//     dateFrom,
//     dateTo,
//     passengers,
//   };
//   const API_GATEWAY_ADDRESS = "http://127.0.0.1:9999";
//   try {
//     let response = await fetch(`${API_GATEWAY_ADDRESS}/v1/trips/search`, {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify(body),
//     });
//     return response.json();
//   } catch (e) {
//     console.error(e);
//     return [];
//   }
// }

const API_GATEWAY_ADDRESS = "http://127.0.0.1:9999";

async function searchHotels(
  location: string | undefined,
  dateFrom: string | undefined,
  dateTo: string | undefined,
  guests: {
    adults: number;
    children: number;
    infants: number;
  }
) {
  const params = new URLSearchParams();
  params.append("toLocation", location || "");
  params.append("fromDate", dateFrom || "");
  params.append("toDate", dateTo || "");
  params.append("adults", guests.adults.toString());
  params.append("children", guests.children.toString());
  params.append("infants", guests.infants.toString());

  try {
    let response = await fetch(
      `${API_GATEWAY_ADDRESS}/v1/hotels/search?${params.toString()}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return await response.json();
  } catch (e) {
    console.error(e);
    return [];
  }
}

async function searchTrips(
  fromLocation: string | undefined,
  toLocation: string | undefined,
  dateFrom: string | undefined,
  dateTo: string | undefined,
  passengers: {
    adults: number;
    children: number;
    infants: number;
  }
) {
  const params = new URLSearchParams();
  params.append("fromLocation", fromLocation || "");
  params.append("toLocation", toLocation || "");
  params.append("fromDate", dateFrom || "");
  params.append("toDate", dateTo || "");
  params.append("adults", passengers.adults.toString());
  params.append("children", passengers.children.toString());
  params.append("infants", passengers.infants.toString());

  try {
    let response = await fetch(
      `${API_GATEWAY_ADDRESS}/v1/trips/search?${params.toString()}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return await response.json();
  } catch (e) {
    console.error(e);
    return [];
  }
}

async function searchTransports(
  fromLocation: string | undefined,
  toLocation: string | undefined,
  dateFrom: string | undefined,
  dateTo: string | undefined,
  passengers: {
    adults: number;
    children: number;
    infants: number;
  }
) {
  const params = new URLSearchParams();
  params.append("fromLocation", fromLocation || "");
  params.append("toLocation", toLocation || "");
  params.append("fromDate", dateFrom || "");
  params.append("toDate", dateTo || "");
  params.append("adults", passengers.adults.toString());
  params.append("children", passengers.children.toString());
  params.append("infants", passengers.infants.toString());

  try {
    let response = await fetch(
      `${API_GATEWAY_ADDRESS}/v1/transport/search?${params.toString()}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return await response.json();
  } catch (e) {
    console.error(e);
    return [];
  }
}

export { searchTrips, searchHotels, searchTransports };
