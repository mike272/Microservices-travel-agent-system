"use client";
import { useState } from "react";
import { TwoLevelInput } from "./two-level-input";
import { SearchButton } from "./search-button";
import { searchTransports } from "@/lib/api/search-api";

import { TwoLevelDatePicker } from "./two-level-date-picker";
import { useRouter } from "next/navigation";
import { useDispatch } from "react-redux";
import { Button, InputNumber, Modal } from "antd";
import { setTransports } from "@/lib/redux/reducers/transportsReducer";
import {
  setDatePreferenceInformation,
  setGuestsPreferenceInformation,
  setLocationPreferenceInformation,
} from "@/lib/redux/reducers/bookingReducer";
type Props = {
  // setTripsData: (data: any) => void;
  // setHotelsData: (data: any) => void;
  // setTransportData: (data: any) => void;
};

export const SearchBar = (props: Props) => {
  // const { setTransportData } = props;
  const [fromLocation, setFromLocation] = useState("Warszawa");
  const [toLocation, setToLocation] = useState("Grecja");
  const router = useRouter();
  const dispatch = useDispatch();

  const [fromDate, setFromDate] = useState<undefined | string>(undefined);
  const [toDate, setToDate] = useState<undefined | string>(undefined);
  const [guests, setGuests] = useState({ adults: 2, children: 0, infants: 0 });
  const [isModalVisible, setIsModalVisible] = useState(false);

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  async function onTransportClick() {
    dispatch(setGuestsPreferenceInformation(guests));
    dispatch(
      setDatePreferenceInformation({
        fromDate: fromDate,
        toDate: toDate,
      })
    );
    dispatch(setLocationPreferenceInformation({ fromLocation, toLocation }));

    let data = await searchTransports(
      fromLocation,
      toLocation,
      fromDate,
      toDate,
      guests
    );
    console.log({ data });
    let transportsResponse = data?.transports ?? [];

    // setTransportData(transportsResponse);
    dispatch(setTransports(transportsResponse));
    router.push("/transports");
  }

  return (
    <div className="flex justify-between items-center rounded-xl border border-black h-20 w-full ml-5 mr-5 p-5 ">
      <TwoLevelInput
        label={"Skąd?"}
        placeholder={"Warszawa"}
        value={fromLocation}
        onChange={setFromLocation}
      />
      <TwoLevelInput
        label={"Dokąd?"}
        placeholder={"Brazylia"}
        value={toLocation}
        onChange={setToLocation}
      />
      <TwoLevelDatePicker
        label={"Data wylotu"}
        placeholder={"06/05/2024"}
        // value={formatTwoDatesToString(whenState.from, whenState.to)}
        onChange={(date, dateString) => {
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
      <Button
        className="bg-white hover:bg-blue-700 text-black font-medium px-4 rounded"
        onClick={showModal}
      >
        Guests
      </Button>
      <Modal
        title="Guests"
        open={isModalVisible}
        okButtonProps={{
          className:
            "bg-blue-500 hover:bg-blue-700 text-white font-bold px-4 rounded",
        }}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        <p>Adults</p>
        <InputNumber
          min={0}
          value={guests.adults}
          onChange={(value) => setGuests({ ...guests, adults: value })}
        />
        <p>Children</p>
        <InputNumber
          min={0}
          value={guests.children}
          onChange={(value) => setGuests({ ...guests, children: value })}
        />
        <p>Infants</p>
        <InputNumber
          min={0}
          value={guests.infants}
          onChange={(value) => setGuests({ ...guests, infants: value })}
        />
      </Modal>
      <SearchButton onClick={onTransportClick} />
    </div>
  );
};
