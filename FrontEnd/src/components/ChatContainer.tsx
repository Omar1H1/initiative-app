import React, { useEffect, useState } from "react";
import { StompSessionProvider, useStompClient, useSubscription } from "react-stomp-hooks";
import { User } from "../types/User.ts";
import { Axios } from "../service/Axios.tsx";

interface Message {
    senderId: number;
    content: string;
}

const api = new Axios().getInstance();

const ChatComponent: React.FC = () => {
    const [userInfo, setUserInfo] = useState<User | null>(null); // Local state for user information
    const [messages, setMessages] = useState<Message[]>([]);
    const [messageContent, setMessageContent] = useState<string>("");
    const receiverId = 1; // Change this to the actual receiver ID as needed

    const stompClient = useStompClient();

    // Fetch connected user information on component mount
    useEffect(() => {
        const fetchConnectedUser  = async () => {
            try {
                const response = await api.get("/api/v1/users/me");
                const user: User = {
                    id: response.data.id,
                    username: response.data.username,
                    firstName: response.data.firstName || "", // Ensure this matches the API response
                    lastName: response.data.lastName || "",   // Ensure this matches the API response
                    role: response.data.role, // Assuming this is a string
                };
                setUserInfo(user); // Set the user information in local state
                console.log("Fetched user:", user);
            } catch (error) {
                console.error("Error fetching user:", error);
            }
        };

        fetchConnectedUser ();
    }, []);

    // Log userInfo whenever it changes
    useEffect(() => {
        console.log("User  info updated:", userInfo);
    }, [userInfo]);

    const handleSendMessage = () => {
        if (!stompClient || !userInfo || !messageContent.trim()) {
            console.error("Missing WebSocket client, user, or message content");
            return;
        }

        stompClient.publish({
            destination: "/app/send",
            body: JSON.stringify({
                senderId: userInfo.id,
                content: messageContent,
                receiverId,
            }),
        });

        console.log("Message sent:", messageContent);
        setMessageContent("");
    };

    useSubscription("/topic/messages", (message: { body: string; }) => {
        const receivedMessage: Message = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
    });

    if (!userInfo) {
        return <div>Loading user information...</div>;
    }

    return (
        <div className="flex flex-col h-screen p-4 bg-gray-100 dark:bg-gray-900">
            <div className="flex-1 overflow-y-auto p-4 bg-white rounded-lg shadow-md dark:bg-gray-800">
                {messages.length === 0 ? (
                    <div className="text-center text-gray-500 dark:text-gray-400">
                        No messages yet.
                    </div>
                ) : (
                    messages.map((msg, index) => (
                        <div key={index} className="mb-2">
                            <strong className="text-blue-600">{msg.senderId}:</strong>
                            <span className="ml-2">{msg.content}</span>
                        </div>
                    ))
                )}
            </div>

            <div className="flex mt-4">
                <input
                    type="text"
                    value={messageContent}
                    onChange={(e) => setMessageContent(e.target.value)}
                    className="flex-1 p-2 border border-gray-300 rounded-l-lg focus:outline-none focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white"
                    placeholder="Type your message..."
                />
                <button
                    onClick={handleSendMessage}
                    className="p-2 bg-blue-600 text-white rounded-r-lg hover:bg-blue-700 transition duration-200 dark:bg-blue-500 dark:hover:bg-blue-600"
                >
                    Send
                </button>
            </div>
        </div>
    );
};

const getToken = () => {
    return localStorage.getItem("token") || sessionStorage.getItem("token");
};

console.log(getToken());

const ChatContainer: React.FC = () => {
    const token = getToken();

    if (!token) {
        console.error("No token found!");
        return <div>Error: Unauthorized</div>;
    }

    return (
        <StompSessionProvider
            url={`http://localhost:8080/ws?token=${token}`} // Include token in query
        >
            <ChatComponent />
        </StompSessionProvider>
    );
};


export default ChatContainer;