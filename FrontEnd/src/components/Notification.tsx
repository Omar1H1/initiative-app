import NotificationItem from "./NotificationItem";
import useNotifications from "../hooks/useNotifications";
import { useAtomValue } from "jotai";
import userAtom from "../service/UserAtom";

const Notification = () => {
    const user = useAtomValue(userAtom);
    const notifications = useNotifications(user?.id ? String(user.id) : undefined);

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

    console.log("Fetched notifications:", notifications);

    return (
        <div className="p-6 pt-28 bg-white dark:bg-gray-800  min-h-screen">
            <h2 className="text-lg font-bold mb-4 text-gray-800 dark:text-white">
                Notifications
            </h2>
            {notifications.length > 0 ? (
                <div className="space-y-4">
                    {notifications.map((notification) => (
                        <NotificationItem
                            key={notification.id}
                            title={notification.title}
                            info={notification.info}
                        />
                    ))}
                </div>
            ) : (
                <p className="space-y-4 text-gray-500 dark:text-gray-400">
                    No notifications available at the moment.
                </p>
            )}
        </div>
    );
};

export default Notification;
