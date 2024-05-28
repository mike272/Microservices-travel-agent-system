import { Transportation } from "@/lib/utils/types";
import { createSlice } from "@reduxjs/toolkit";

interface TransportState {
  transports: Transportation[];
  selectedFromtransport: Transportation | null;
  selectedTotransport: Transportation | null;
}

const initialState: TransportState = {
  transports: [],
  selectedFromtransport: null,
  selectedTotransport: null,
};

const transportsSlice = createSlice({
  name: "transports",
  initialState,
  reducers: {
    setTransports: (state, action) => {
      state.transports = action.payload;
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
