import React from "react";

import { useContext, useEffect, useState } from "react";
import { Axios } from "../service/Axios.tsx";
import { Profile } from "../service/Profiles.ts";
import isLogged from "../service/LoginState.tsx";
import { FaHeart } from "react-icons/fa";
import { IoCloseCircle } from "react-icons/io5";
import { useAtomValue } from "jotai";
import loginAtom from "../service/LoginState.tsx";

const api = new Axios().getInstance();

const Profiles = () => {
  const [profiles, setProfiles] = useState<Profile[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [likedProfiles, setLikedProfiles] = useState<Profile[]>([]);
  const [dislikedProfiles, setDislikedProfiles] = useState<Profile[]>([]);

  const token = useAtomValue(loginAtom);

  const fetchProfiles = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await api.get<Profile[]>("/api/v1/profiles");
      setProfiles(response.data);
    } catch (err) {
      console.error("Error fetching profiles:", err);
      setError("Failed to fetch profiles. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (token && !profiles.length) {
      fetchProfiles();
    }
  }, [token, profiles.length]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  const handleLike = (profile: Profile) => {
    setLikedProfiles((prevLikes) => [...prevLikes, profile]);
    setProfiles((prevProfiles) =>
      prevProfiles.filter((p) => p.id !== profile.id),
    );
  };

  const handleDislike = (profile: Profile) => {
    setDislikedProfiles((prevDislikes) => [...prevDislikes, profile]);
    setProfiles((prevProfiles) =>
      prevProfiles.filter((p) => p.id !== profile.id),
    );
  };

  return (
    <div className="p-4 relative">
      <h1 className="text-2xl font-bold mb-4 px-20">Profiles</h1>
      <div className="relative w-full max-w-full sm:max-w-2xl mx-auto mt-40 flex items-center justify-center">
        {profiles.map((profile, index) => (
          <div
            key={profile.id}
            className={`absolute w-full sm:w-[90%] md:w-[70%] p-6 bg-white rounded-xl shadow-2xl transform transition-all duration-300 ease-in-out ${index === profiles.length - 1 ? "scale-100 z-10" : "scale-90 z-0"
              }`}
          >
            <img
              src={"https://via.placeholder.com/150"}
              alt={`${profile.firstName} ${profile.lastName}`}
              className="w-full h-48 object-cover rounded-lg"
            />
            <div className="mt-4">
              <h2 className="text-2xl font-semibold">
                {profile.firstName} {profile.lastName}
              </h2>
              <p className="text-gray-600">{profile.username}</p>
              <p className="text-gray-500">{profile.email}</p>
            </div>
            <div className="mt-4 flex justify-between">
              <button
                className="w-20 bg-red-500 text-white p-2 rounded-md hover:bg-red-600 flex items-center justify-center"
                onClick={() => handleDislike(profile)}
              >
                <IoCloseCircle />
              </button>
              <button
                className="w-20 bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600 flex items-center justify-center"
                onClick={() => handleLike(profile)}
              >
                <FaHeart />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Profiles;
