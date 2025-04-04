import { useState } from "react";
import Signup from "./Signup.tsx";
import { Axios } from "../service/Axios.tsx";

const api = new Axios().getInstance();

const CodePage = () => {
  const queryParameters = new URLSearchParams(window.location.search);
  const pathCode: string = queryParameters.get("code") as string;
  const [code, setCode] = useState(pathCode);
  const [validCode, setValidCode] = useState(false);
  const [userInfo, setUserInfo] = useState({
    firstName: "",
    lastName: "",
    email: "",
    role: "",
  });

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const codeToActive = {
        activationCode: code,
      };

      const response = await api.post(
        "/api/v1/users/preauthenticate",
        codeToActive,
      );

      setUserInfo({
        firstName: response.data.firstName,
        lastName: response.data.lastName,
        email: response.data.email,
        role: response.data.role,
      });
      setValidCode(true);
      console.log("ok" + response.data.email);
      console.log(response.data.firstName);
    } catch (error) {
      console.error("Error during pre-registration:", error);
    }
  };

  return (
    <div className="dark:bg-gray-800 bg-bkg min-h-screen flex items-center justify-center px-4 py-8">
      {!validCode ? (
        <div className="w-full max-w-md bg-white dark:bg-gray-800 bg-opacity-90 backdrop-filter backdrop-blur-lg rounded-lg shadow-lg p-8">
          <h2 className="text-lg sm:text-xl md:text-2xl lg:text-3xl font-bold text-gray-800 mb-6 text-center dark:text-white">
            Saisissez votre code
          </h2>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label
                htmlFor="code"
                className="block text-gray-700 font-bold mb-2 dark:text-white"
              >
                Code
              </label>
              <input
                type="text"
                id="code"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                value={code}
                onChange={(e) => setCode(e.target.value)}
              />
            </div>
            <div>
              <button
                type="submit"
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300 w-full"
              >
                Envoyer
              </button>
            </div>
          </form>
        </div>
      ) : (
        <Signup userInfo={userInfo} />
      )}
    </div>
  );
};

export default CodePage;
