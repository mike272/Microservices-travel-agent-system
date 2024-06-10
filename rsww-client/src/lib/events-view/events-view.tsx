import React, { useEffect, useRef, useState } from "react";
import { Button, notification } from "antd";
import { EventType } from "../utils/types";
import { Client, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { setReservationStatus } from "../redux/reducers/bookingReducer";

const EventsView = () => {
  const [events, setEvents] = useState<EventType[]>([]);
  const [isToggled, setIsToggled] = useState(true);
  const [client, setClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState(false);
  const bookingId = useSelector(
    (state: RootState) => state.booking.reservationId
  );
  let localBookingId = bookingId;
  useEffect(() => {
    localBookingId = bookingId;
  }, [bookingId]);
  const dispatch = useDispatch();

  const subscriptionRef = useRef(null);

  useEffect(() => {
    if (client && isConnected) {
      return;
    }

    var url = process.env.NEXT_PUBLIC_API_GATEWAY_ADDRESS + "/subscribe";
    var stompClient = null;
    var socket = new SockJS(url);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
      if (!isConnected) {
        console.log("Connected: " + frame);
        if (subscriptionRef.current) {
          return;
        }

        // Subscribe and store subscription in ref
      }
      setIsConnected(true);
    });

    stompClient.onConnect = () => {
      console.log("WebSocket is connected.");
      subscriptionRef.current = subscriptionRef.current = stompClient.subscribe(
        "/topic/messages",
        (message) => {
          if (message.body) {
            const eventData = JSON.parse(message.body) as EventType;
            console.log({
              reservationId: eventData?.tripReservationId,
              bookingId,
              localBookingId,
            });
            if (
              eventData?.tripReservationId === localBookingId ||
              eventData?.tripReservationId === localBookingId - 1 ||
              true
            ) {
              if (eventData.status === "PAID") {
                notification.open({
                  message: "Your reservation has been confirmed",
                  type: "success",
                });
              } else if (eventData.status === "CONFIRMED") {
                notification.open({
                  message: "Your reservation has been confirmed",
                  type: "success",
                });
                dispatch(setReservationStatus("CONFIRMED"));
              } else if (eventData.status === "CREATED") {
                notification.open({
                  message: "Your reservation has been created",
                  type: "info",
                });

                dispatch(setReservationStatus("CREATED"));
              }
            }
            console.log({ eventData });
            setEvents((prevEvents) => [...prevEvents, eventData]);

            notification.open({
              message: eventData.textContent,
              type: eventData.type.toLowerCase() as
                | "info"
                | "success"
                | "error"
                | "warning",
            });
          }
        }
      );
    };

    stompClient.onStompError = (error) => {
      console.log(`WebSocket error: ${error}`);
      console.log({ error });
    };

    stompClient.activate();

    setClient(stompClient);

    return () => {
      if (client) {
        client.deactivate();
      }
      if (subscriptionRef.current) {
        subscriptionRef.current.unsubscribe();
        subscriptionRef.current = null;
      }
    };
  }, []);

  function EventRow({ type, textContent }: EventType) {
    return (
      <div
        style={{
          backgroundColor: getBackgroundColor(type),
          color: "black",
          borderRadius: "10px",
          boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)",
          padding: "10px",
          margin: "10px 5px",
        }}
      >
        {textContent}
      </div>
    );
  }

  function ToggledView() {
    return (
      <div
        style={{
          width: "300px",
          height: "100vh",
          overflowY: "scroll",
          position: "relative",
          flex: "0 0 auto",
          borderLeft: "1px solid grey",
        }}
      >
        <Button
          type="primary"
          onClick={() => setIsToggled(false)}
          style={{ position: "absolute", right: 0, top: "20px" }}
        >
          {isToggled ? ">>" : "<<"}
        </Button>
        {isToggled &&
          events.map((event, index) => (
            <EventRow
              key={index}
              type={event.type}
              textContent={event.textContent}
              tripReservationId={event?.tripReservationId}
              status={event?.status}
            />
          ))}
      </div>
    );
  }

  function HiddenView() {
    return (
      <div className="absolute w-10 h-10 top-5 right-0">
        <Button onClick={() => setIsToggled(true)}>{"<<"}</Button>
      </div>
    );
  }

  return isToggled ? <ToggledView /> : <HiddenView />;
};

function getColor(type: "INFO" | "SUCCESS" | "ERROR" | "WARNING") {
  switch (type) {
    case "INFO":
      return "blue";
    case "SUCCESS":
      return "green";
    case "ERROR":
      return "red";
    case "WARNING":
      return "orange";
    default:
      return "black";
  }
}

function getBackgroundColor(type: "INFO" | "SUCCESS" | "ERROR" | "WARNING") {
  switch (type) {
    case "INFO":
      return "#D3D3D3"; // light grey
    case "SUCCESS":
      return "#98FB98"; // pastel green
    case "ERROR":
      return "#FFB6C1"; // pastel red
    case "WARNING":
      return "#FFD700"; // pastel orange
    default:
      return "white";
  }
}

export { EventsView };
