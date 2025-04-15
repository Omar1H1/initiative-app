import Messages from "./Messages";

const Chat = () => {
  return (
    <div className="m-40">
      <div className="mt-16 flex flex-col h-screen bg-white dark:bg-gray-900">
        <div className="flex-grow p-2 rounded-md overflow-hidden">
          <Messages />
        </div>
      </div>
    </div>
  );
};

export default Chat;
