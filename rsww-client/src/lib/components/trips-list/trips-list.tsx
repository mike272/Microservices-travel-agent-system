import { Trip } from "@/lib/utils/types";

type Props = {
  tripsList: Array<Trip>;
};

export const TripList = (props: Props) => {
  const { tripsList } = props;

  const TripItemRow = (trip: Trip) => {
    return (
      <div className="flex justify-between items-center rounded-xl border border-black h-20 w-full ml-5 mr-5 p-5 ">
        <div>{trip?.name ?? "Trip name not available"}</div>
        <div>{trip?.description ?? "Trip description not available"}</div>
      </div>
    );
  };
  return (
    <div className="flex flex-col items-center w-full gap-4">
      {tripsList?.length >0 ? tripsList.map((trip, index) => (
        <TripItemRow {...trip} key={index} />
      )): <div>Loading...</div>}
    </div>
  );
};
