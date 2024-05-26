type Trip = {
  id: number;
  name: string;
  description: string;
  location: string;
  fromDate: string;
  toDate: string;
  hotel: Hotel;
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
  fromDate: string;
  toDate: string;
  price: number;
  rating: number;
  reviews: number;
  image: string;
};

type Transportation = {
  id: number;
  name: string;
  description: string;
  location: string;
  fromDate: string;
  fromTime: string;
  toDate: string;
  price: number;
  rating: number;
  reviews: number;
  image: string;
};

export type { Trip, Hotel, Transportation };
