import { Hotel, Room } from "@/lib/utils/types";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface HotelsState {
  hotels: Hotel[];
  selectedHotel: Hotel | null;
  selectedRooms: Room[];
}

const initialState: HotelsState = {
  hotels: [],
  selectedHotel: null,
  // {
  //   id: 812,
  //   name: "default hotel",
  //   starsNum: 5,
  //   arechildrenAllowed: true,
  //   city: "Zakynthos",
  //   country: "Grecja",
  //   description: "test description",
  //   minPrice: 200,
  // } as Hotel,
  selectedRooms: [],
};

const hotelsSlice = createSlice({
  name: "hotels",
  initialState,
  reducers: {
    setHotels: (state, action: PayloadAction<Hotel[]>) => {
      state.hotels = action.payload;
    },
    setSelectedHotel: (state, action: PayloadAction<Hotel>) => {
      state.selectedHotel = action.payload;
    },
    setSelectedRooms: (state, action: PayloadAction<Room[]>) => {
      state.selectedRooms = action.payload;
    },
  },
});

export const { setHotels, setSelectedHotel, setSelectedRooms } =
  hotelsSlice.actions;

export default hotelsSlice.reducer;
