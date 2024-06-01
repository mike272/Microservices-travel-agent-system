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
  stars: number;
  about: string;
  areKidsAllowed: boolean;
  rooms: number;
  city: string;
  description: string;
  location: string;
  fromDate: Date;
  toDate: Date;
  price: number;
  rating: number;
  reviews: number;
  image: string;
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
