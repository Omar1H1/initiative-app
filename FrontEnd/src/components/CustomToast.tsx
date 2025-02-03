import { useEffect, useState } from "react";
import { toast } from "react-hot-toast";
import { Axios } from "../service/Axios.tsx";

const api = new Axios().getInstance();

interface CustomToastProps {
    title: string;
    message: string;
    userId: number;
    t: any;
}

const CustomToast = ({ title, message, userId, t }: CustomToastProps) => {
    const [photo, setPhoto] = useState<string>("");

    useEffect(() => {
        const fetchProfilePhoto = async () => {
            try {
                const response = await api.get(`/api/v1/users/${userId}/profile-image`, { responseType: "blob" });
                const imageUrl = URL.createObjectURL(response.data);
                setPhoto(imageUrl);
            } catch (err) {
                console.error("Error fetching profile image:", err);
                setPhoto("https://via.placeholder.com/150");
            }
        };

        fetchProfilePhoto();
    }, [userId]);

    return (
        <div
            className={`${
                t.visible ? "animate-enter" : "animate-leave"
            } max-w-md w-full bg-white dark:bg-gray-900 shadow-lg rounded-lg pointer-events-auto flex ring-1 ring-black ring-opacity-5 dark:ring-opacity-50`}
        >
            <div className="flex-1 w-0 p-4">
                <div className="flex items-start">
                    <div className="flex-shrink-0 pt-0.5">
                        <img
                            className="h-10 w-10 rounded-full"
                            src={photo}
                            alt="Profile"
                        />
                    </div>
                    <div className="ml-3 flex-1">
                        <p className="text-sm font-medium text-gray-900 dark:text-gray-100">{title}</p>
                        <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">{message}</p>
                    </div>
                </div>
            </div>
            <div className="flex border-l border-gray-200 dark:border-gray-700">
                <button
                    onClick={() => toast.dismiss(t.id)}
                    className="w-full border border-transparent rounded-none rounded-r-lg p-4 flex items-center justify-center text-sm font-medium text-indigo-600 hover:text-indigo-500 dark:text-indigo-400 dark:hover:text-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                    Close
                </button>
            </div>
        </div>
    );
};

export const showCustomToast = (title: string, message: string, userId: number) => {
    toast.custom((t) => <CustomToast title={title} message={message} userId={userId} t={t} />);
};

export default CustomToast;
