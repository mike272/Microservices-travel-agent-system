import { Transportation } from "@/lib/utils/types";
import { createSlice } from "@reduxjs/toolkit";

interface TransportState {
  transports: Transportation[];
  selectedFromtransport: Transportation | null;
  selectedTotransport: Transportation | null;
}

const initialState: TransportState = {
  transports: [],
  // selectedFromtransport: {
  //   id: 8759,
  //   destinationCity: "Zakynthos",
  //   departureCity: "Gdańsk",
  //   destinationCountry: "Grecja",
  //   departureCountry: "Polska",
  //   transportType: "FLIGHT",
  //   departureDate: new Date(),
  //   basePrice: 80,
  //   availablePlaces: 10,
  //   totalPlaces: 10,
  // },
  // selectedTotransport: {
  //   id: 9259,
  //   departureCity: "Zakynthos",
  //   destinationCity: "Gdańsk",
  //   departureCountry: "Grecja",
  //   destinationCountry: "Polska",
  //   transportType: "FLIGHT",
  //   departureDate: new Date(),
  //   basePrice: 80,
  //   availablePlaces: 10,
  //   totalPlaces: 10,
  // },
  selectedFromtransport: null,
  selectedTotransport: null,
};

const transportsSlice = createSlice({
  name: "transports",
  initialState,
  reducers: {
    setTransports: (state, action) => {
      state.transports = action.payload.map((transport) => {
        const date = new Date(transport.departureDate);
        return {
          ...transport,
          departureDate: date,
          arrivalDate: date,
        };
      });
    },
    setFromTransportation: (state, action) => {
      state.selectedFromtransport = action.payload;
    },
    setToTransportation: (state, action) => {
      state.selectedTotransport = action.payload;
    },
  },
});

export const { setFromTransportation, setToTransportation, setTransports } =
  transportsSlice.actions;
export default transportsSlice.reducer;
