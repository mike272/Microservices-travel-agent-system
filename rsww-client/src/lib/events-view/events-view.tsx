import React, { useEffect, useState } from "react";
import { Button, notification } from "antd";
import { EventType } from "../utils/types";
import { Client, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const EventsView = () => {
  const [events, setEvents] = useState<EventType[]>([]);
  const [isToggled, setIsToggled] = useState(true);
  const [client, setClient] = useState<Client | null>(null);

  useEffect(() => {
    // const socket = new SockJS(
    //   process.env.NEXT_PUBLIC_API_GATEWAY_ADDRESS + "/subscribe"
    // );
    // const stompClient = new Client({
    //   webSocketFactory: () => socket,
    // });
    var url = process.env.NEXT_PUBLIC_API_GATEWAY_ADDRESS + "/subscribe";
    var stompClient = null; //Stomp.client(url);

    var socket = new SockJS(url);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      // setConnected(true);
      console.log("Connected: " + frame);
      stompClient.subscribe("/topic/messages", function (messageOutput) {
        console.log(JSON.parse(messageOutput.body));
      });
    });

    stompClient.onConnect = () => {
      console.log("WebSocket is connected.");

      stompClient.subscribe("/topic/messages", (message) => {
        if (message.body) {
          const eventData = JSON.parse(message.body) as EventType;
          setEvents((prevEvents) => [...prevEvents, eventData]);

          notification.open({
            message: eventData.message,
            type: eventData.type.toLowerCase() as
              | "info"
              | "success"
              | "error"
              | "warning",
          });
        }
      });
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
    };
  }, []);

  function EventRow({ type, message }: EventType) {
    return (
      <div
        style={{
          backgroundColor: getBackgroundColor(type),
          color: "black",
          borderRadius: "10px",
          boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)",
          padding: "10px",
          margin: "10px 0",
        }}
      >
        {message}
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
            <EventRow key={index} type={event.type} message={event.message} />
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
