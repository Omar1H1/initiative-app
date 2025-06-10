import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Axios } from "../service/Axios";
import { User } from "../types/User";

const api = new Axios().getInstance();

const ConversationList = () => {
  const loggedUser = localStorage.getItem("user");
  const logged: User | null = loggedUser ? JSON.parse(loggedUser) : null;
  const navigate = useNavigate();

  const [conversationIds, setConversationIds] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [conversationPartners, setConversationPartners] = useState<{
    [key: string]: { user: User | null; photo: string | null };
  }>({});

  useEffect(() => {
    if (!logged?.id) {
      setLoading(false);
      setError("User not logged in.");
      return;
    }

    const fetchConversations = async () => {
      try {
        const response = await api.get(
          `/api/v1/messages/user/${logged.id}/conversations`
        );
        setConversationIds(response.data);
      } catch (err) {
        console.error("Error fetching conversation IDs:", err);
        setError("Failed to load conversations.");
      } finally {
        console.log(conversationIds);
        setLoading(false);
      }
    };

    fetchConversations();
  }, [logged?.id]);

  useEffect(() => {
    const fetchPartnerDetails = async () => {
      const newPartners: { [key: string]: { user: User | null; photo: string | null } } = {};
      const partnerPromises = conversationIds.map(async (convId) => {
        const ids = convId.split("_");
        const partnerId = ids[0] === String(logged?.id) ? ids[1] : ids[0];

        try {
          const userResponse = await api.get(`/api/v1/users/${partnerId}`);
          const partnerUser: User = userResponse.data;

          let partnerPhotoUrl: string | null = null;
          try {
            const photoResponse = await api.get(
              `/api/v1/users/${partnerId}/profile-image`,
              { responseType: "blob" }
            );
            partnerPhotoUrl = URL.createObjectURL(photoResponse.data);
          } catch {
            partnerPhotoUrl = `https://ui-avatars.com/api/?name=${partnerUser.firstName}+${partnerUser.lastName}&background=random&font-size=0.5&rounded=true`;
          }

          newPartners[convId] = { user: partnerUser, photo: partnerPhotoUrl };
        } catch (err) {
          console.error(`Error fetching details for partner ${partnerId}:`, err);
          newPartners[convId] = { user: null, photo: null };
        }
      });
      await Promise.all(partnerPromises);
      setConversationPartners(newPartners);
    };

    if (conversationIds.length > 0) {
      fetchPartnerDetails();
    }
  }, [conversationIds, logged?.id]);

  const handleConversationClick = (conversationId: string) => {
    const ids = conversationId.split("_");
    const partnerId = ids[0] === String(logged?.id) ? ids[1] : ids[0];
    navigate(`/chat/${partnerId}`);
  };

  if (loading) {
    return (
      <div className="text-center text-xl text-gray-700 dark:text-gray-300 mt-20">
        Loading conversations...
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center text-xl text-red-500 mt-20">
        Error: {error}
      </div>
    );
  }

  if (conversationIds.length === 0) {
    return (
      <div className="px-6 mt-5 pt-24 pb-4 h-screen box-border flex flex-col items-center justify-center">
        <h2 className="text-3xl font-bold text-gray-800 dark:text-white mb-4">No Conversations Yet</h2>
        <p className="text-lg text-gray-600 dark:text-gray-400">
          Looks like you haven't started any chats or received messages.
        </p>
        <p className="text-lg text-gray-600 dark:text-gray-400 mt-2">
          Find some matches and start a new conversation!
        </p>
      </div>
    );
  }

  return (
    <div className="px-6 mt-5 pt-24 pb-4 h-screen box-border flex flex-col items-center">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 dark:text-white">Your Conversations</h2>
      <div className="w-full max-w-2xl space-y-4">
        {conversationIds.map((convId) => {
          const partnerData = conversationPartners[convId];
          const partnerUser = partnerData?.user;
          const partnerPhoto = partnerData?.photo;

          if (!partnerUser) {
            return (
              <div
                key={convId}
                className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-md flex items-center space-x-4"
              >
                <div className="flex-shrink-0">
                  <img
                    src="https://ui-avatars.com/api/?name=Unknown+User&background=random&font-size=0.5&rounded=true"
                    alt="Unknown User"
                    className="w-12 h-12 rounded-full object-cover"
                  />
                </div>
                <div>
                  <div className="font-semibold text-gray-800 dark:text-white">
                    Unknown User
                  </div>
                  <div className="text-sm text-gray-500 dark:text-gray-400">
                    Conversation ID: {convId} (details failed to load)
                  </div>
                </div>
              </div>
            );
          }

          return (
            <div
              key={convId}
              className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-md flex items-center space-x-4 cursor-pointer hover:bg-gray-50 dark:hover:bg-gray-700 transition duration-200 transform hover:scale-[1.02] active:scale-[0.98]"
              onClick={() => handleConversationClick(convId)}
            >
              <div className="flex-shrink-0">
                <img
                  src={partnerPhoto || `https://ui-avatars.com/api/?name=${partnerUser.firstName}+${partnerUser.lastName}&background=random&font-size=0.5&rounded=true`}
                  alt={`${partnerUser.firstName} ${partnerUser.lastName}`}
                  className="w-12 h-12 rounded-full object-cover border border-gray-200 dark:border-gray-600"
                />
              </div>
              <div>
                <div className="font-semibold text-gray-800 dark:text-white text-lg">
                  {partnerUser.firstName} {partnerUser.lastName}
                </div>
                <div className="text-sm text-gray-500 dark:text-gray-400">
                  Click to view chat
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ConversationList;

