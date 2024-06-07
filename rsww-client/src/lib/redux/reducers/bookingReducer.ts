import { PayloadAction, createSlice } from "@reduxjs/toolkit";

interface BookingState {
  adults: number;
  children: number;
  infants: number;
  fromDate: string;
  toDate: string;
  fromLocation: string;
  toLocation: string;
  reservationId: number | null;
  reservationStatus: string;
}

const initialState: BookingState = {
  adults: 0,
  children: 0,
  infants: 0,
  fromDate: "",
  toDate: "",
  fromLocation: "",
  toLocation: "",
  reservationId: null,
  reservationStatus: "",
};

const bookingSlice = createSlice({
  name: "booking",
  initialState,
  reducers: {
    setBooking: (state, action: PayloadAction<BookingState>) => {
      state = action.payload;
    },
    setPreferenceInformation: (
      state,
      action: PayloadAction<{
        adults: number;
        children: number;
        infants: number;
        fromDate: string;
        toDate: string;
        fromLocation: string;
        toLocation: string;
      }>
    ) => {
      state.adults = action.payload.adults;
      state.children = action.payload.children;
      state.infants = action.payload.infants;
      state.fromDate = action.payload.fromDate;
      state.toDate = action.payload.toDate;
      state.fromLocation = action.payload.fromLocation;
      state.toLocation = action.payload.toLocation;
    },
    setGuestsPreferenceInformation: (
      state,
      action: PayloadAction<{
        adults: number;
        children: number;
        infants: number;
      }>
    ) => {
      state.adults = action.payload.adults;
      state.children = action.payload.children;
      state.infants = action.payload.infants;
    },
    setDatePreferenceInformation: (
      state,
      action: PayloadAction<{
        fromDate: string;
        toDate: string;
      }>
    ) => {
      state.fromDate = action.payload.fromDate;
      state.toDate = action.payload.toDate;
    },
    setLocationPreferenceInformation: (
      state,
      action: PayloadAction<{
        fromLocation: string;
        toLocation: string;
      }>
    ) => {
      state.fromLocation = action.payload.fromLocation;
      state.toLocation = action.payload.toLocation;
    },
    setReservationId: (state, action: PayloadAction<number>) => {
      state.reservationId = action.payload;
    },
    setReservationStatus: (state, action: PayloadAction<string>) => {
      state.reservationStatus = action.payload;
    },
  },
});

export const {
  setBooking,
  setPreferenceInformation,
  setGuestsPreferenceInformation,
  setDatePreferenceInformation,
  setLocationPreferenceInformation,
  setReservationId,
  setReservationStatus,
} = bookingSlice.actions;
export default bookingSlice.reducer;
