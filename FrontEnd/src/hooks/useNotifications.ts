import { useEffect } from "react";
import { showCustomToast } from "../components/CustomToast.tsx";
import { NotificationType } from "../types/Notification.ts";
import { useAtomValue, useSetAtom } from "jotai";
import notificationAtom from "../service/NotificationAtom.tsx";

const useNotifications = (userId: string | undefined) => {
  const notifications = useAtomValue(notificationAtom);
  const setNotifications = useSetAtom(notificationAtom);

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
        const newNotification: NotificationType = {
          id: match.id,
          title:
            match.status === "pending"
              ? "Nouvelle demande de match"
              : "Demande de match acceptée",
          info:
            match.status === "pending"
              ? `Nouvelle demande de match de ${match.demander.firstName} ${match.demander.lastName}`
              : `Votre demande de match avec ${match.receiver?.firstName || "inconnu"} ${match.receiver?.lastName || ""} a bien été acceptée`,
          demanderId: match.demander.id,
          seen: false,
          status: match.status,
        };

        setNotifications((prev: NotificationType[]) => {
          const existingNotifs = new Map(
            prev.map((n: NotificationType) => [n.id, n]),
          );
          existingNotifs.set(newNotification.id, newNotification);

          const updated = Array.from(existingNotifs.values());
          localStorage.setItem("notifications", JSON.stringify(updated));

          return updated;
        });

        showCustomToast(
          newNotification.title,
          newNotification.info,
          newNotification.demanderId,
        );
      } catch (error) {
        console.error("Error parsing notification event:", error);
      }
    };

    eventSource.onerror = (error) => {
      console.error("Error with notification EventSource:", error);
      eventSource.close();
    };
    const messageEventSource = new EventSource(
      `http://localhost:8080/api/v1/notifications/messages?userId=${userId}`,
    );

    messageEventSource.onmessage = (event) => {
      console.log("Message notification event received:", event.data);
      try {
        const message = JSON.parse(event.data);
        const newNotification = {
          id: message.id,
          title: "Nouveau message",
          info: `Vous avez reçu un nouveau message`,
          demanderId: message.sender,
          seen: false,
          status: "message",
        };

        showCustomToast(
          newNotification.title,
          newNotification.info,
          newNotification.demanderId,
        );
      } catch (error) {
        console.error("Error parsing message notification event:", error);
      }
    };

    messageEventSource.onerror = (error) => {
      console.error("Error with message notification EventSource:", error);
      messageEventSource.close();
    };

    return () => {
      eventSource.close();
      messageEventSource.close();
    };
  }, [userId, setNotifications]);

  useEffect(() => {
    const savedNotifications = localStorage.getItem("notifications");
    if (savedNotifications) {
      setNotifications(JSON.parse(savedNotifications));
    }
  }, [setNotifications]);

  const markAsRead = (id: number) => {
    setNotifications((prev) => {
      const currentNotifications = prev || [];
      const updated = currentNotifications.map((n) =>
        n.id === id ? { ...n, seen: true } : n,
      );
      return updated;
    });
  };

  return { notifications, markAsRead, setNotifications };
};

export default useNotifications;
