import React, { useEffect, useState } from "react";
import { Button, notification } from "antd";
import { EventType } from "../utils/types";

const EventsView = () => {
  const [events, setEvents] = useState<EventType[]>([]);
  const [isToggled, setIsToggled] = useState(false);

  useEffect(() => {
    const socket = new WebSocket(
      process.env.API_GATEWAY_ADDRESS + "/websocket"
    );

    socket.onmessage = (event) => {
      const eventData = JSON.parse(event.data) as EventType;
      setEvents((prevEvents) => [...prevEvents, eventData]);

      notification.open({
        message: eventData.message,
        type: eventData.type.toLowerCase() as
          | "info"
          | "success"
          | "error"
          | "warning",
      });
    };

    return () => {
      socket.close();
    };
  }, []);

  return (
    <div
      style={{
        backgroundColor: "green",
        width: isToggled ? "300px" : "0",
        height: "100vh",
        overflowY: "scroll",
        position: "relative",
        flex: "0 0 auto",
      }}
    >
      <Button
        type="primary"
        onClick={() => setIsToggled(!isToggled)}
        style={{ position: "absolute", left: 0 }}
      >
        {isToggled ? ">>" : "<<"}
      </Button>
      {isToggled &&
        events.map((event, index) => (
          <div key={index} style={{ color: getColor(event.type) }}>
            {event.message}
          </div>
        ))}
    </div>
  );
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

export { EventsView };
