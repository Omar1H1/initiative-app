import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { Axios } from "../service/Axios.tsx";
import { useNavigate } from "react-router-dom";

const api = new Axios().getInstance();

const ResetPassword = () => {
  const queryParameters = new URLSearchParams(window.location.search);
  const pathCode: any = queryParameters.get("code");

  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [passwordsMatch, setPasswordsMatch] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      setPasswordsMatch(true);
    } else {
      const data = {
        activationCode: pathCode,
        password,
      };

      try {
        const response = await api.post("/api/v1/users/resetpassword", data);
        console.log("User  has been created with success:", response.data);
        navigate("/login");
      } catch (error) {
        console.error("Error during creation:", error);
      }
    }
  };

  const handleShowPasswordToggle = () => {
    setShowPassword((prev) => !prev);
  };

  const handleShowConfirmPasswordToggle = () => {
    setShowConfirmPassword((prev) => !prev);
  };

  return (
    <div className="bg-gray-100 h-screen flex flex-col items-center justify-center dark:bg-gray-900">
      <div className="w-full max-w-md bg-white shadow-md px-8 py-6 rounded-lg dark:bg-gray-800">
        <h2 className="text-center text-2xl font-bold text-gray-800 dark:text-white pb-10">
          Réinitialiser le mot de passe
        </h2>
        <form onSubmit={handleSubmit}>
          {passwordsMatch && (
            <p className="text-red-500 text-xs mb-4">
              Les mots de passe ne correspondent pas.
            </p>
          )}
          <div className="mb-4">
            <label
              htmlFor="password"
              className="block text-sm font-medium text-gray-700 dark:text-gray-300 pb-3"
            >
              Votre nouveau mot de passe
            </label>
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                id="password"
                className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-800 leading-tight focus:outline-none focus:ring ${
                  passwordsMatch ? "border-red-500" : "border-gray-300"
                } dark:border-gray-600 dark:bg-gray-700 dark:text-white`}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <button
                type="button"
                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300"
                onClick={handleShowPasswordToggle}
              >
                {showPassword ? <FaEye /> : <FaEyeSlash />}
              </button>
            </div>
          </div>
          <div className="mb-4">
            <label
              htmlFor="confirm-password"
              className="block text-sm font-medium text-gray-700 dark:text-gray-300 pb-3"
            >
              Confirmer votre mot de passe
            </label>
            <div className="relative">
              <input
                type={showConfirmPassword ? "text" : "password"}
                id="confirm-password"
                className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-800 leading-tight focus:outline-none focus:ring ${
                  passwordsMatch ? "border-red-500" : "border-gray-300"
                } dark:border-gray-600 dark:bg-gray-700 dark:text-white`}
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
              <button
                type="button"
                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300"
                onClick={handleShowConfirmPasswordToggle}
              >
                {showConfirmPassword ? <FaEye /> : <FaEyeSlash />}
              </button>
            </div>
          </div>
          <div className="mb-6">
            <button
              type="submit"
              className="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300"
            >
              Envoyer
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ResetPassword;
