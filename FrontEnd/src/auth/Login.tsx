import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Axios } from "../service/Axios";
import loginAtom from "../service/LoginState";
import { CgSpinnerAlt } from "react-icons/cg";
import { useSetAtom } from "jotai";
import { User } from "../types/User";
import { FaEyeSlash, FaEye } from "react-icons/fa";
import userAtom from "../service/UserAtom";

const api = new Axios().getInstance();

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();
  const setToken = useSetAtom(loginAtom);
  const setUser = useSetAtom(userAtom);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);

    const data = { email, password };
    try {
      const response = await api.post("/api/v1/users/authenticate", data);
      const user: User = {
        id: response.data.id,
        username: response.data.username,
        firstName: response.data.firstname,
        lastName: response.data.lastname,
        role: response.data.role,
      };
      setUser(user);
      localStorage.setItem("user", JSON.stringify(user));
      if (rememberMe) {
        localStorage.setItem("token", response.data.token);
        setToken(response.data.token);
      } else {
        sessionStorage.setItem("token", response.data.token);
        setToken(response.data.token);
      }
      navigate("/");
    } catch (error) {
      console.error("Error during login:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleContactCLick = () => {
    navigate("/contact");
  };



  const handleForgetPasswordClick = () => {
    navigate("/passwordrecovery");
  };

  return (
      <div className="bg-bkg h-screen flex flex-col items-center justify-center dark:bg-gray-900 rounded-lg dark:bg-opacity-90 shadow-1-lg">

        <div className="w-full max-w-md bg-white shadow-md px-8 py-6 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg dark:bg-gray-800">
          <h2 className="text-center text-2xl font-bold text-content dark:text-white">
            Login
          </h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label
                  htmlFor="email"
                  className="block text-sm font-bold text-gray-700 mb-2 dark:text-gray-300"
              >
                Email
              </label>
              <input
                  type="email"
                  id="email"
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-200 dark:bg-gray-700 dark:text-white"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="mb-4">
              <label
                  htmlFor="password"
                  className="block text-sm font-bold text-gray-700 mb-2 dark:text-gray-300"
              >
                Password
              </label>
              <div className="relative">
                <input
                    type={showPassword ? "text" : "password"}
                    id="password"
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-200 dark:bg-gray-700 dark:text-white"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button
                    type="button"
                    className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300"
                    onClick={() => setShowPassword((prev) => !prev)}
                >
                  {showPassword ? <FaEye /> : <FaEyeSlash />}
                </button>
              </div>
            </div>

            <div className="mb-4 flex items-center">
              <input
                  type="checkbox"
                  id="rememberme"
                  className="mr-2"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
              />
              <label
                  htmlFor="rememberme"
                  className="text-sm text-gray-700 dark:text-gray-300"
              >
                Remember Me
              </label>
            </div>

            <div className="mb-6">
              <button
                  type="submit"
                  className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300 flex justify-center items-center"
              >
                {isLoading ? <CgSpinnerAlt className="animate-spin h-6 w-6" /> : "Login"}
              </button>
            </div>
            {/* Forget Password */}
            <div className="text-center mb-4">
              <button
                  type="button"
                  onClick={handleForgetPasswordClick}
                  className="text-sm sm:text-base text-blue-500 hover:text-blue-700"
              >
                Mot de passe oublié? Cliquez ici
              </button>
            </div>

            {/* Contact Button */}
            <p className="text-center text-sm sm:text-base text-gray-700 mb-2 dark:text-white">
              Vous n'avez pas encore vos identifiants ?
            </p>
            <div className="text-center">
              <button
                  type="button"
                  onClick={handleContactCLick}
                  className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300"
              >
                Contactez-nous
              </button>
            </div>
          </form>
        </div>
      </div>
  );
};

export default Login;
