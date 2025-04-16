import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Axios } from "../service/Axios";
import { User } from "../types/User";

import { IoMdSend } from "react-icons/io";

const api = new Axios().getInstance();

const formatDate = (dateString: string) => {
  const messageDate = new Date(dateString);
  const now = new Date();

  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const yesterday = new Date(today);
  yesterday.setDate(today.getDate() - 1);

  const isToday = messageDate.toDateString() === today.toDateString();
  const isYesterday = messageDate.toDateString() === yesterday.toDateString();

  if (isToday) {
    return `Aujourd'hui, ${messageDate.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
      timeZone: "Europe/Paris",
    })}`;
  } else if (isYesterday) {
    return "hier";
  } else {
    return messageDate.toLocaleString([], {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
      timeZone: "Europe/Paris",
    });
  }
};

const Messages = () => {
  const loggedUser = localStorage.getItem("user");
  const logged: User | null = loggedUser ? JSON.parse(loggedUser) : null;
  const [chatWith, setChatWith] = useState<User | null>(null);
  const { userId } = useParams<{ userId: string }>();
  const [partnerPhoto, setPartnerPhoto] = useState<string | null>(null);
  const [myPhoto, setMyPhoto] = useState<string | null>(null);
  const [conversation, setConversation] = useState<any[]>([]);

  useEffect(() => {
    if (userId) {
      fetchProfilePhoto(Number(userId), setPartnerPhoto);
      if (logged?.id) {
        fetchProfilePhoto(Number(logged.id), setMyPhoto);
      }
      const convId =
        Number(userId) < Number(logged?.id)
          ? `${userId}_${logged?.id}`
          : `${logged?.id}_${userId}`;
      fetchConversation(convId);
      fetchTalkingWithIn(Number(userId));
    }
  }, [userId, logged?.id]);

  const fetchProfilePhoto = async (
    id: number,
    setPhotoState: React.Dispatch<React.SetStateAction<string | null>>,
  ) => {
    try {
      const response = await api.get(`/api/v1/users/${id}/profile-image`, {
        responseType: "blob",
      });
      const imageUrl = URL.createObjectURL(response.data);
      setPhotoState(imageUrl);
    } catch (err) {
      console.error("Error fetching profile image:", err);
      setPhotoState(
        `https://ui-avatars.com/api/?name=User  &background=random&font-size=0.5&rounded=true`,
      );
    }
  };

  const fetchConversation = async (convId: string) => {
    try {
      const response = await api.get(`/api/v1/messages/conversation/${convId}`);
      setConversation(response.data);
    } catch (err) {
      console.error("Error fetching conversation:", err);
    }
  };

  const fetchTalkingWithIn = async (userId: number) => {
    try {
      const response = await api.get(`/api/v1/users/${userId}`);
      setChatWith(response.data);
    } catch (err) {
      console.error("Error fetching user", err);
    }
  };

  return (
    <div className="flex-grow w-full max-w-4xl mx-auto flex flex-col h-3/4">
      <div className="w-full p-4 bg-purple-600 dark:bg-gray-800 shadow-lg rounded-xl">
        <div className="flex items-center">
          <div className="flex-shrink-0">
            <img
              src={
                partnerPhoto ??
                `https://ui-avatars.com/api/?name=${chatWith?.firstName}+${chatWith?.lastName}&background=random&font-size=0.5&rounded=true`
              }
              alt="User "
              className="w-12 h-12 object-cover rounded-full border-2 border-white"
            />
          </div>
          <div className="ml-4">
            <div className="text-lg text-white font-semibold">
              {chatWith?.firstName} {chatWith?.lastName}
            </div>
            <div className="flex items-center">
              <div className="w-2 h-2 bg-green-300 rounded-full"></div>
              <div className="text-sm text-gray-200 ml-1">Online</div>
            </div>
          </div>
        </div>
      </div>
      <div className="flex-grow bg-gray-100 dark:bg-gray-700 my-2 p-4 overflow-y-auto rounded-lg shadow-inner">
        {conversation.length === 0 ? (
          <div className="text-center text-gray-500">Start a conversation!</div>
        ) : (
          conversation
            .slice()
            .reverse()
            .map((message, index) => (
              <div
                key={index}
                className={`flex items-end w-full ${message.sender === logged?.id ? "justify-end" : "justify-start"}`}
              >
                {message.sender !== logged?.id && (
                  <img
                    className="w-8 h-8 m-2 rounded-full"
                    src={
                      partnerPhoto ??
                      `https://ui-avatars.com/api/?name=User   &background=random&font-size=0.5&rounded=true`
                    }
                    alt="avatar"
                  />
                )}
                <div
                  className={`p-3 ${message.sender === logged?.id ? "bg-blue-600 text-white" : "bg-gray-800 text-gray-200"} mx-3 my-1 rounded-2xl ${message.sender === logged?.id ? "rounded-bl-none" : "rounded-br-none"} sm:w-3/4 md:w-2/3`}
                >
                  <div className="text-sm">{message.content}</div>
                  <div className="text-xs text-gray-300 mt-1 text-right">
                    {formatDate(message.date)}
                  </div>
                </div>
                {message.sender === logged?.id && (
                  <img
                    className="w-8 h-8 m-2 rounded-full"
                    src={
                      myPhoto ??
                      `https://ui-avatars.com/api/?name=${logged?.firstName}+${logged?.lastName}&background=random&font-size=0.5&rounded=true`
                    }
                    alt="avatar"
                  />
                )}
              </div>
            ))
        )}
        <div className="mt-0 sticky bottom-0 bg-transparent p-2 z-10">
          <div className="flex items-center gap-2">
            <input
              type="text"
              placeholder="Votre message"
              className="flex-grow p-2 border border-gray-300 rounded-lg focus:outline-none dark:text-black focus:ring-2 focus:ring-purple-600"
            />
            <button className="dark:text-white text-purple-900 hover:text-purple-700">
              <IoMdSend size={24} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Messages;
