"use client";
import { useState } from "react";
import { TwoLevelInput } from "./two-level-input";
import { SearchButton } from "./search-button";
import { searchHotels, searchTrips } from "@/lib/api/search-api";
import {
  formatTwoDatesToString,
  getDateNDaysFromToday,
} from "@/lib/utils/util-functions";
import { TwoLevelDatePicker } from "./two-level-date-picker";
import { useRouter } from "next/navigation";
import { useDispatch } from "react-redux";
import { setHotels } from "@/lib/redux/reducers/hotelsReducer";
type Props = {
  setTripsData: (data: any) => void;
  setHotelsData: (data: any) => void;
};

export const SearchBar = (props: Props) => {
  const { setTripsData, setHotelsData } = props;
  const [fromState, setFromState] = useState("");
  const [toState, setToState] = useState("");
  const router = useRouter();
  const dispatch = useDispatch();

  const [fromDate, setFromDate] = useState<undefined | string>(undefined);
  const [toDate, setToDate] = useState<undefined | string>(undefined);

  async function onClick() {
    // let data = await searchTrips(fromState, toState, fromDate, toDate, {
    //   adults: 1,
    //   children: 0,
    //   infants: 0,
    // });
    let data = await searchHotels(fromState, fromDate, toDate, 3);
    let hotels = data?.hotels ?? [];
    // setTripsData(data);
    console.log(hotels);
    setHotelsData(hotels);
    dispatch(setHotels(hotels));
    router.push("/hotels");
  }
  return (
    <div className="flex justify-between items-center rounded-xl border border-black h-20 w-full ml-5 mr-5 p-5 ">
      <TwoLevelInput
        label={"Skąd?"}
        placeholder={"Warszawa"}
        value={fromState}
        onChange={setFromState}
      />
      <TwoLevelInput
        label={"Dokąd?"}
        placeholder={"Brazylia"}
        value={toState}
        onChange={setToState}
      />
      <TwoLevelDatePicker
        label={"Data wylotu"}
        placeholder={"06/05/2024"}
        // value={formatTwoDatesToString(whenState.from, whenState.to)}
        onChange={(date, dateString) => {
          console.log({ date, dateString });
          setFromDate(dateString);
        }}
      />
      <TwoLevelDatePicker
        label={"Data powrotu"}
        placeholder={"20/05/2024"}
        // value={formatTwoDatesToString(whenState.from, whenState.to)}
        onChange={(date, dateString) => {
          setToDate(dateString);
        }}
      />
      <SearchButton onClick={onClick} />
    </div>
  );
};
