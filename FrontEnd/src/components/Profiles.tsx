import { useEffect, useState } from "react";
import { Axios } from "../service/Axios.tsx";
import { Profile } from "../types/Profiles.ts";
import { FaHeart } from "react-icons/fa";
import { IoCloseCircle } from "react-icons/io5";
import { useAtomValue } from "jotai";
import loginAtom from "../service/LoginState.tsx";
import { FaBriefcase } from "react-icons/fa";
import { CgProfile } from "react-icons/cg";

const api = new Axios().getInstance();

const Profiles = () => {
  const [profiles, setProfiles] = useState<Profile[]>([]);
  const [, setIsLoading] = useState<boolean>(false);
  const [, setError] = useState<string | null>(null);
  const [, setLikedProfiles] = useState<Profile[]>([]);
  const [, setDislikedProfiles] = useState<Profile[]>([]);
  const [photos, setPhotos] = useState<{ [key: number]: string }>({});

  const token = useAtomValue(loginAtom);

  const fetchProfiles = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await api.get<Profile[]>("/api/v1/profiles");
      setProfiles(response.data);
      console.log(response.data);
    } catch (err) {
      console.error("Error fetching profiles:", err);
      setError("Failed to fetch profiles. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const fetchProfilePhoto = async (id: number) => {
    try {
      const response = await api.get(`/api/v1/users/${id}/profile-image`, {
        responseType: "blob",
      });
      const imageUrl = URL.createObjectURL(response.data);

      setPhotos((prevPhotos) => ({ ...prevPhotos, [id]: imageUrl }));
    } catch (err) {
      console.error("Error fetching profile image:", err);
    }
  };

  useEffect(() => {
    if (token && !profiles.length) {
      fetchProfiles();
    }
  }, [token, profiles.length]);

  useEffect(() => {
    if (profiles.length) {
      profiles.forEach((profile) => {
        fetchProfilePhoto(profile.id);
      });
    }
  }, [profiles]);

  const handleLike = async (profile: Profile) => {
    setLikedProfiles((prevLikes) => [...prevLikes, profile]);
    setProfiles((prevProfiles) =>
      prevProfiles.filter((p) => p.id !== profile.id),
    );
    const matchRequest = {
      receiverId: profile.id,
    };

    await api.post("/api/v1/match", matchRequest);
  };

  const handleDislike = (profile: Profile) => {
    setDislikedProfiles((prevDislikes) => [...prevDislikes, profile]);
    setProfiles((prevProfiles) =>
      prevProfiles.filter((p) => p.id !== profile.id),
    );
  };

  return (
    <div className="p-4 relative dark:bg-gray-900 h-screen">
      <h1 className="text-2xl font-bold mb-4 px-20 flex items-center justify-center py-20 text-gray-800 dark:text-white">
        Profiles
      </h1>
      <div className="pt-10 relative w-full max-w-full sm:max-w-2xl mx-auto mt-40 flex items-center justify-center">
        {profiles.map(
          (profile: Profile, index: number) =>
            profile.isActive && (
              <div
                key={profile.id}
                className={`absolute w-full sm:w-[90%] md:w-[70%] p-6 bg-white rounded-xl shadow-2xl transform transition-all duration-300 ease-in-out dark:bg-gray-800 ${
                  index === profiles.length - 1
                    ? "scale-100 z-10"
                    : "scale-90 z-0"
                }`}
              >
                <img
                  src={photos[profile.id] ?? "https://via.placeholder.com/150"}
                  alt={`${profile.firstName} ${profile.lastName}`}
                  className="w-fit h-48 object-fill rounded-lg"
                />
                <div className="mt-4">
                  <div className="flex items-center pt-2 pb-2">
                    <CgProfile className="mr-2" />
                    <h2 className="text-2xl font-bold text-gray-800 dark:text-white">
                      {profile.firstName} {profile.lastName}
                    </h2>
                  </div>
                  <div className="flex items-center pt-2 pb-2">
                    <FaBriefcase className="mr-2" />
                    <p className="text-gray-600 font-semibold dark:text-gray-300">
                      {profile.sectorOfActivity}
                    </p>
                  </div>
                  <p className="text-gray-500 dark:text-gray-400">
                    {profile.projectDescription}
                  </p>
                  <p className="text-gray-500 dark:text-gray-400"></p>
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
            ),
        )}
      </div>
    </div>
  );
};

export default Profiles;
