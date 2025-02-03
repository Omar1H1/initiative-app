import { useEffect, useState } from "react";
import { showCustomToast } from "../components/CustomToast.tsx";

type Notification = {
  id: number;
  title: string;
  info: string;
  userId: number;
  seen: boolean;
};

const useNotifications = (userId: string | undefined) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);

  useEffect(() => {
    if (!userId) return;

    console.log(`Subscribing to notifications for user ID: ${userId}`);
    const eventSource = new EventSource(
        `http://localhost:8080/api/v1/notifications/match?userId=${userId}`,
    );

    eventSource.onmessage = (event) => {
      console.log("Notification event received:", event.data);
      try {
        const match = JSON.parse(event.data);
        const newNotification: Notification = {
          id: match.id,
          title: "Nouvelle demande de match",
          info: `Nouvelle demande de match de ${match.demander.firstName} ${match.demander.lastName}`,
          userId: match.demander.id,
          seen: false,
        };


        setNotifications((prev) => {
          const existingNotifs = new Map(prev.map((n) => [n.id, n]));
          existingNotifs.set(newNotification.id, newNotification);

          const updated = Array.from(existingNotifs.values());
          localStorage.setItem("notifications", JSON.stringify(updated));

          return updated;
        });

        showCustomToast(newNotification.title, newNotification.info, newNotification.userId);
      } catch (error) {
        console.error("Error parsing notification event:", error);
      }
    };

    eventSource.onerror = () => {
      console.error("Error with notification EventSource.");
      eventSource.close();
    };

    return () => eventSource.close();
  }, [userId]);

  useEffect(() => {
    const savedNotifications = localStorage.getItem("notifications");
    if (savedNotifications) {
      setNotifications(JSON.parse(savedNotifications));
    }
  }, []);

  const markAsRead = (id: number) => {
    setNotifications((prev) => {
      const updated = prev.map((n) =>
          n.id === id ? { ...n, seen: true } : n
      );
      localStorage.setItem("notifications", JSON.stringify(updated));
      return updated;
    });
  };


  return { notifications, markAsRead };
};

export default useNotifications;
