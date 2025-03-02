import { useNavigate } from "react-router-dom";

function NotFound() {
  const navigate = useNavigate();
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100 dark:bg-gray-900">
      <h1 className="text-4xl font-bold text-gray-800 dark:text-white mb-4">
        404
      </h1>
      <p className="text-lg text-gray-600 dark:text-gray-300 mb-8">
        🫢 Oops, nous n'avons pas pu trouver ce que vous cherchez !!
      </p>
      <a
        onClick={() => navigate(-1)}
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded dark:bg-blue-600 dark:hover:bg-blue-700"
      >
        Revenir en arrière
      </a>
    </div>
  );
}

export default NotFound;
