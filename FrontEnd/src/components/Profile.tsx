import React, { useState, useEffect } from "react";
import { User } from "../types/User";
import { Axios } from "../service/Axios";

const api = new Axios().getInstance();

const Profile: React.FC = () => {
  const loggedUser: string | null = localStorage.getItem("user");
  const logged: User | null = loggedUser ? JSON.parse(loggedUser) : null;
  const [photo, setPhoto] = useState<string | null>(null);

  useEffect(() => {
    if (logged?.id) {
      fetchProfilePhoto(logged.id);
    }
  }, [logged?.id]);

  const fetchProfilePhoto = async (id: number) => {
    try {
      const response = await api.get(`/api/v1/users/${id}/profile-image`, {
        responseType: "blob",
      });
      const imageUrl = URL.createObjectURL(response.data);
      setPhoto(imageUrl);
    } catch (err) {
      console.error("Error fetching profile image:", err);
      setPhoto(
        `https://ui-avatars.com/api/?name=${logged?.firstName}+${logged?.lastName}&background=random&font-size=0.5&rounded=true`,
      );
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 dark:bg-gray-900 py-8 px-4 sm:px-6 lg:px-8">
      <div className="bg-white dark:bg-gray-800 shadow-md rounded-lg p-6 sm:p-8 w-full max-w-md">
        <div className="relative w-24 h-24 sm:w-32 sm:h-32 mx-auto rounded-full overflow-hidden mb-4 sm:mb-6">
          <img
            src={`https://ui-avatars.com/api/?name=${logged?.firstName}+${logged?.lastName}&background=random&font-size=0.5&rounded=true`}
            alt={`${logged?.firstName} ${logged?.lastName}`}
            className="w-full h-full object-cover"
          />
        </div>

        <div className="text-center">
          <h2 className="text-xl sm:text-2xl font-semibold text-gray-800 dark:text-white mb-1 sm:mb-2">
            {logged
              ? `${logged.firstName} ${logged.lastName}`
              : "No user logged in"}
          </h2>
          <p className="text-gray-500 dark:text-gray-400 mb-2 sm:mb-3">
            @{logged?.username}
          </p>
        </div>
      </div>
    </div>
  );
};

export default Profile;
