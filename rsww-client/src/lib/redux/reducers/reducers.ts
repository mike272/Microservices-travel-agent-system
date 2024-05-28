import { combineReducers } from "redux";
import hotelsReducer from "./hotelsReducer";
import transportsReducer from "./transportsReducer";
import bookingReducer from "./bookingReducer";

export default combineReducers({
  hotels: hotelsReducer,
  transports: transportsReducer,
  booking: bookingReducer,
});
