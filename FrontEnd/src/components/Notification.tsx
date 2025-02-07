import NotificationItem from "./NotificationItem";
import useNotifications from "../hooks/useNotifications";
import { useAtomValue } from "jotai";
import userAtom from "../service/UserAtom";
import { ImCheckmark } from "react-icons/im";
import { RxCross2 } from "react-icons/rx";
import React, {useEffect} from "react";
import { Axios } from "../service/Axios.tsx";
import { IoTrashBinOutline } from "react-icons/io5";


const api = new Axios().getInstance();

const Notification = () => {
    const user = useAtomValue(userAtom);
    const { notifications = [], setNotifications } = useNotifications(user?.id ? String(user.id) : undefined);
    if (!user?.id) {
        return (
            <p className="text-red-500 dark:text-red-400">
                User ID is missing. Please log in to view notifications.
            </p>
        );
    }

    if (!notifications) {
        return (
            <p className="text-gray-700 dark:text-gray-300">
                Loading notifications...
            </p>
        );
    }

    const handleAccept = async (e: React.MouseEvent<SVGElement>, id: number) => {
        e.preventDefault();
        try {
            await api.put(`/api/v1/match/accept/${id}`);
            setNotifications((prev) => {
                const updatedNotifications = prev.filter(notification => notification.id !== id);
                localStorage.setItem("notifications", JSON.stringify(updatedNotifications));
                return updatedNotifications;
            });
        } catch (error) {
            console.error("Error accepting notification:", error);
        }
    };


    const handleReject = async (e: React.MouseEvent<SVGElement>, id: number) => {
        e.preventDefault();

        try {
            const response = await api.put(`/api/v1/match/reject/${id}`);
            console.log("Reject response:", response.data);
            setNotifications((prev) => {
                const updatedNotifications = prev.filter(notification => notification.id !== id);
                localStorage.setItem("notifications", JSON.stringify(updatedNotifications));
                return updatedNotifications;
            });
        } catch (error) {
            console.error("Error rejecting notification:", error);
        }
    };

    const handleRemoveNotification = async (e: React.MouseEvent<SVGElement>, id: number) => {
        e.preventDefault();

        try {
            setNotifications((prev) => {
                const updatedNotifications = prev.filter(notification => notification.id !== id);
                localStorage.setItem("notifications", JSON.stringify(updatedNotifications));
                return updatedNotifications;
            });
        } catch (error) {
            console.error("Error rejecting notification:", error);
        }
    };



    const handleMarkAsSeen = async (e: React.MouseEvent<HTMLDivElement>, id: number) => {
        e.preventDefault();

        try {
            await api.put(`/api/v1/match/seen/${id}`);
            setNotifications((prev) => {
                const updatedNotifications = prev.map(notification =>
                    notification.id === id ? { ...notification, seen: true } : notification
                );
                localStorage.setItem("notifications", JSON.stringify(updatedNotifications));
                return updatedNotifications;
            });
        } catch (error) {
            console.error("Error marking notification as seen:", error);
        }
    };

    useEffect(() => {
        const savedNotifications = localStorage.getItem("notifications");
        if (savedNotifications) {
            setNotifications(JSON.parse(savedNotifications));
        }
    }, []);

    console.log("Fetched notifications:", notifications);

    return (
        <div className="p-6 pt-28 bg-white dark:bg-gray-800 min-h-screen">
            <h2 className="text-lg font-bold mb-4 text-gray-800 dark:text-white">
                Notifications
            </h2>
            {notifications.length > 0 ? (
                <div className="space-y-4">
                    {notifications.map((notification) => (

                        <div key={notification.id} className="flex items-center justify-between">
                            <NotificationItem
                                title={notification.title}
                                info={notification.info}
                                seen={notification.seen}
                                onClick={(e)  => handleMarkAsSeen(e, notification.id)}
                            />
                            <div className="flex space-x-2">
                                {notification.status === "pending" && (
                                    <>
                                        <ImCheckmark
                                            className="text-green-500 cursor-pointer"
                                            onClick={(e) => handleAccept(e, notification.id)}
                                        />
                                        <RxCross2
                                            className="text-red-500 cursor-pointer"
                                            onClick={(e) => handleReject(e, notification.id)}
                                        />
                                    </>
                                )}
                                <IoTrashBinOutline
                                    className="text-red-500 cursor-pointer"
                                    onClick={(e) => handleRemoveNotification(e, notification.id)}
                                />
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <p className="space-y-4 text-gray-500 dark:text-gray-400">
                    Aucune notification disponible pour le moment.
                </p>
            )}
        </div>
    );
};

export default Notification;