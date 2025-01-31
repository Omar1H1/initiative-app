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
  const [emailError, setEmailError] = useState(false);
  const [passwordError, setPasswordError] = useState(false);
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
      console.log(user);
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
      setEmailError(true);
      setPasswordError(true);
    } finally {
      setIsLoading(false);
    }
  };

  const handleContactClick = () => {
    navigate("/contact");
  };

  const handleForgetPasswordClick = () => {
    navigate("/passwordrecovery");
  };

  return (
      <div className="bg-gray-100 h-screen flex flex-col items-center justify-center dark:bg-gray-900">
        <div className="w-full max-w-md bg-white shadow-md px-8 py-6 rounded-lg dark:bg-gray-800">
          <h2 className="text-center text-2xl font-bold text-gray-800 dark:text-white">
            Connexion
          </h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label
                  htmlFor="email"
                  className="block text-sm font-medium text-gray-700 dark:text-gray-300"
              >
                Adresse email
              </label>
              <input
                  type="email"
                  id="email"
                  className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-800 leading-tight focus:outline-none focus:ring ${
                      emailError
                          ? "border-red-500 focus:ring-red-300"
                          : "border-gray-300 focus:ring-blue-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white"
                  }`}
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                    setEmailError(false);
                  }}
              />
              {emailError && (
                  <p className="text-red-500 text-xs mt-1">Email incorrect</p>
              )}
            </div>
            <div className="mb-4">
              <label
                  htmlFor="password"
                  className="block text-sm font-medium text-gray-700 dark:text-gray-300"
              >
                Mot de passe
              </label>
              <div className="relative">
                <input
                    type={showPassword ? "text" : "password"}
                    id="password"
                    className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-800 leading-tight focus:outline-none focus:ring ${
                        passwordError
                            ? "border-red-500 focus:ring-red-300"
                            : "border-gray-300 focus:ring-blue-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white"
                    }`}
                    value={password}
                    onChange={(e) => {
                      setPassword(e.target.value);
                      setPasswordError(false);
                    }}
                />
                <button
                    type="button"
                    className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300"
                    onClick={() => setShowPassword((prev) => !prev)}
                >
                  {showPassword ? <FaEye /> : <FaEyeSlash />}
                </button>
              </div>
              {passwordError && (
                  <p className="text-red-500 text-xs mt-1">Mot de passe incorrect</p>
              )}
            </div>
            <div className="mb-4 flex items-center">
              <input
                  type="checkbox"
                  id="rememberme"
                  className="mr-2 text-blue-500 dark:text-blue-300"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
              />
              <label
                  htmlFor="rememberme"
                  className="text-sm text-gray-700 dark:text-gray-300"
              >
                Se souvenir de moi
              </label>
            </div>
            <div className="mb-6">
              <button
                  type="submit"
                  className="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300"
              >
                {isLoading ? <CgSpinnerAlt className="animate-spin h-6 w-6" /> : "Se connecter"}
              </button>
            </div>
            <div className="text-center mb-4">
              <button
                  type="button"
                  onClick={handleForgetPasswordClick}
                  className="text-sm text-blue-500 hover:text-blue-700"
              >
                Mot de passe oublié?
              </button>
            </div>
            <p className="text-center text-sm text-gray-700 dark:text-gray-300">
              Vous n'avez pas encore vos identifiants ?
            </p>
            <div className="text-center mt-2">
              <button
                  type="button"
                  onClick={handleContactClick}
                  className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300"
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
