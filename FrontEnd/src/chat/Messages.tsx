import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Axios } from "../service/Axios";
import { User } from "../types/User";

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
    return messageDate.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    });
  } else if (isYesterday) {
    return "hier";
  } else {
    return messageDate.toLocaleString([], {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
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
        `https://ui-avatars.com/api/?name=User &background=random&font-size=0.5&rounded=true`,
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
    <div className="flex-grow h-full flex flex-col">
      <div className="w-full h-15 p-1 bg-purple-600 dark:bg-gray-800 shadow-md rounded-xl rounded-bl-none rounded-br-none">
        <div className="flex p-2 align-middle items-center">
          <div className="border rounded-lg border-transparent p-1/2">
            <img
              src={
                partnerPhoto ??
                `https://ui-avatars.com/api/?name=User &background=random&font-size=0.5&rounded=true`
              }
              alt="User "
              className="w-14 h-14 object-cover rounded-lg"
            />
          </div>
          <div className="flex-grow p-2">
            <div className="text-md text-gray-50 font-semibold">
              {chatWith?.firstName} {chatWith?.lastName}
            </div>
            <div className="flex items-center">
              <div className="w-2 h-2 bg-green-300 rounded-full"></div>
              <div className="text-xs text-gray-50 ml-1">Online</div>
            </div>
          </div>
        </div>
      </div>
      <div className="w-full h-28 flex-grow bg-gray-100 dark:bg-gray-900 my-2 p-2 overflow-y-auto">
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
                    className="w-8 h-8 m-3 rounded-full"
                    src={
                      partnerPhoto ??
                      `https://ui-avatars.com/api/?name=User  &background=random&font-size=0.5&rounded=true`
                    }
                    alt="avatar"
                  />
                )}
                <div
                  className={`p-3 ${message.sender === logged?.id ? "bg-blue-600 text-white" : "bg-gray-800"} mx-3 my-1 rounded-2xl ${message.sender === logged?.id ? "rounded-bl-none" : "rounded-br-none"} sm:w-3/4 md:w-3/6`}
                >
                  <div className="text-white dark:text-gray-200">
                    {message.content}
                  </div>
                  <div className="text-xs text-white mt-1 text-right">
                    {formatDate(message.date)}
                  </div>
                </div>
                {message.sender === logged?.id && (
                  <img
                    className="w-8 h-8 m-3 rounded-full"
                    src={
                      myPhoto ??
                      `https://ui-avatars.com/api/?name=User  &background=random&font-size=0.5&rounded=true`
                    }
                    alt="avatar"
                  />
                )}
              </div>
            ))
        )}
      </div>
    </div>
  );
};

export default Messages;
