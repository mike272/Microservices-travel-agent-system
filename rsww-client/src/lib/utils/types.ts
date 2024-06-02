type Trip = {
  id: number;
  name: string;
  description: string;
  location: string;
  fromDate: Date;
  toDate: Date;
  hotel: Hotel;
};

type Room = {
  id: number;
  name: string;
  price: number;
  capacity: number;
};

type Hotel = {
  id: number;
  name: string;
  starsNum: number;
  arechildrenAllowed: boolean;
  rooms: number;
  city: string;
  country: string;
  description: string;
  minPrice: number;
};

type Transportation = {
  id: number;
  destinationCity: string;
  departureCity: string;
  destinationCountry: string;
  departureCountry: string;
  transportType: string;
  departureDate: Date;
  basePrice: number;
  availablePlaces: number;
  totalPlaces: number;
};

type EventType = {
  textContent: string;
  type: "INFO" | "SUCCESS" | "ERROR" | "WARNING";
};

export type { Trip, Hotel, Transportation, EventType, Room };
