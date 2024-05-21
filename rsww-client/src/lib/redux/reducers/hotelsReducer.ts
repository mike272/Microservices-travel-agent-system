import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface Hotel {
  id: number;
  location: string;
  description: string;
  name: string;
}

interface HotelsState {
  hotels: Hotel[];
}

const initialState: HotelsState = {
  hotels: [],
};

const hotelsSlice = createSlice({
  name: "hotels",
  initialState,
  reducers: {
    setHotels: (state, action: PayloadAction<Hotel[]>) => {
      state.hotels = action.payload;
    },
  },
});

export const { setHotels } = hotelsSlice.actions;

export default hotelsSlice.reducer;
