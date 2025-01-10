import { useEffect, useState } from "react";
import CustomToast from "../components/CustomToast.tsx";

// Define the notification type
type Notification = {
  id: number;
  title: string;
  info: string;
  seen: boolean;
};

const useNotifications = (userId: string | undefined) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);

  useEffect(() => {
    if (!userId) {
      console.error("No user ID provided to fetch notifications.");
      return;
    }

    console.log(`Subscribing to notifications for user ID: ${userId}`);
    const eventSource = new EventSource(
      `http://localhost:8080/api/v1/notifications/match?userId=${userId}`,
    );

    eventSource.onmessage = (event) => {
      console.log("Notification event received:", event.data);
      try {
        const match = JSON.parse(event.data);
        const newNotification: Notification = {
          id: Date.now(),
          title: "New match request",
          info: `New match request from ${match.demander.username}`,
          seen: false,
        };

        setNotifications((prev) => {
          const updated = [newNotification, ...prev];
          localStorage.setItem("notifications", JSON.stringify(updated));
          return updated;
        });

        CustomToast(newNotification.title, newNotification.info);
      } catch (error) {
        console.error("Error parsing notification event:", error);
      }
    };

    eventSource.onerror = () => {
      console.error("Error with notification EventSource.");
      eventSource.close();
    };

    return () => {
      console.log("Closing EventSource for notifications.");
      eventSource.close();
    };
  }, [userId]);

  useEffect(() => {
    const savedNotifications = localStorage.getItem("notifications");
    if (savedNotifications) {
      setNotifications(JSON.parse(savedNotifications));
    }
  }, []);

  return notifications;
};

export default useNotifications;
