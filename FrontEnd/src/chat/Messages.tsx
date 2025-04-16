import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { Axios } from "../service/Axios";
import { User } from "../types/User";
import { Client } from "@stomp/stompjs";
import { IoMdSend } from "react-icons/io";
import DateService from "../service/DateService";

const api = new Axios().getInstance();

const formatDate = DateService;

const Messages = () => {
  const loggedUser = localStorage.getItem("user");
  const logged: User | null = loggedUser ? JSON.parse(loggedUser) : null;
  const token = sessionStorage.getItem("token") || "";
  const { userId } = useParams<{ userId: string }>();

  const [chatWith, setChatWith] = useState<User | null>(null);
  const [partnerPhoto, setPartnerPhoto] = useState<string | null>(null);
  const [myPhoto, setMyPhoto] = useState<string | null>(null);
  const [conversation, setConversation] = useState<any[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const messageEndRef = useRef<HTMLDivElement | null>(null);
  const stompClient = useRef<Client | null>(null);
  const [isStompConnected, setIsStompConnected] = useState(false);

  const convId =
    Number(userId) < Number(logged?.id)
      ? `${userId}_${logged?.id}`
      : `${logged?.id}_${userId}`;

  useEffect(() => {
    if (userId) {
      fetchProfilePhoto(Number(userId), setPartnerPhoto);
      if (logged?.id) fetchProfilePhoto(Number(logged.id), setMyPhoto);
      fetchConversation(convId);
      fetchTalkingWithIn(Number(userId));
    }
  }, [userId, logged?.id, convId, conversation]);

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
    } catch {
      setPhotoState(
        "https://ui-avatars.com/api/?name=User &background=random&font-size=0.5&rounded=true",
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

  useEffect(() => {
    if (!logged?.id || !token) return;

    const client = new Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: { Authorization: `Bearer ${token}` },
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        setIsStompConnected(true);
        client.subscribe(`/user/${logged.id}/queue/messages`, (message) => {
          const body = JSON.parse(message.body);
          setConversation((prev) => [...prev, body]);
        });
      },
      onDisconnect: () => setIsStompConnected(false),
      onStompError: (frame) => {
        console.error("Broker error: ", frame.headers["message"]);
      },
      onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
      },
    });

    client.activate();
    stompClient.current = client;

    return () => {
      if (client.connected) client.deactivate();
    };
  }, [logged?.id, token]);

  const handleSend = () => {
    if (
      !newMessage.trim() ||
      !stompClient.current ||
      !logged?.id ||
      !isStompConnected
    ) {
      return;
    }

    const msgObj = {
      receiver: Number(userId),
      content: newMessage,
    };

    stompClient.current.publish({
      destination: "/send",
      body: JSON.stringify(msgObj),
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    setConversation((prev) => [
      ...prev,
      { ...msgObj, sender: logged?.id, date: new Date().toISOString() },
    ]);
    setNewMessage("");
  };

  useEffect(() => {
    messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [conversation]);

  return (
    <div className="flex-grow w-full max-w-4xl mx-auto flex flex-col h-[90vh]">
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
          <div className="text-center text-black dark:text-white">
            Dis bonjour à {chatWith?.firstName} .... 😊
          </div>
        ) : (
          conversation
            .slice()
            .reverse()
            .map((message, index) => (
              <div
                key={index}
                className={`flex items-end w-full ${
                  message.sender === logged?.id
                    ? "justify-end"
                    : "justify-start"
                }`}
              >
                {message.sender !== logged?.id && (
                  <img
                    className="w-8 h-8 m-2 rounded-full"
                    src={
                      partnerPhoto ??
                      `https://ui-avatars.com/api/?name=User &background=random&font-size=0.5&rounded=true`
                    }
                    alt="avatar"
                  />
                )}
                <div
                  className={`p-3 ${
                    message.sender === logged?.id
                      ? "bg-blue-600 text-white"
                      : "bg-gray-800 text-gray-200"
                  } mx-3 my-1 rounded-2xl ${
                    message.sender === logged?.id
                      ? "rounded-bl-none"
                      : "rounded-br-none"
                  } sm:w-3/4 md:w-2/3`}
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
        <div ref={messageEndRef} />
        <div className="mt-4 sticky bottom-0 bg-transparent p-2 z-10">
          <div className="flex items-center gap-2">
            <input
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Votre message"
              className="flex-grow p-2 border border-gray-300 rounded-lg focus:outline-none dark:text-black focus:ring-2 focus:ring-purple-600"
              onKeyDown={(e) => {
                if (e.key === "Enter") handleSend();
              }}
            />
            <button
              onClick={handleSend}
              className={`${
                isStompConnected
                  ? "text-purple-900 hover:text-purple-700"
                  : "opacity-50 cursor-not-allowed"
              }`}
              disabled={!isStompConnected}
            >
              <IoMdSend size={24} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Messages;
