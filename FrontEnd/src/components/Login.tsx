import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Axios } from "../service/Axios.tsx";
import isLogged from "../service/LoginState.tsx";

import { FaEyeSlash, FaEye } from "react-icons/fa";

const api = new Axios().getInstance();

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();
  const [, setToken] = useContext(isLogged);

  const handleSubmit = async (e: Event) => {
    e.preventDefault();

    const data = {
      email,
      password,
    };

    try {
      const response = await api.post("/api/v1/users/authenticate", data);
      console.log("User has been created with success:", response.data);
      if (rememberMe) {
        localStorage.setItem("token", response.data.token);
        setToken(response.data.token);
      } else {
        sessionStorage.setItem("token", response.data.token);
        setToken(response.data.token);
      }
      navigate("/");
    } catch (error) {
      console.error("Error during creation:", error);
    }
  };

  const handleContactCLick = () => {
    navigate("/contact");
  };

  const handleShowPasswordToggle = () => {
    setShowPassword((prev) => !prev);
  };

  const handleForgetPasswordClick = () => {
    navigate("/passwordrecovery");
  };

  return (
    <>
      <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
        <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
          <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label
                htmlFor="email"
                className="block text-gray-700 text-lg font-bold mb-2"
              >
                Email
              </label>
              <input
                type="email"
                id="email"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div className="mb-4">
              <label
                htmlFor="password"
                className="block text-gray-700 text-lg font-bold mb-2"
              >
                mot de passe
              </label>
              <div className="relative">
                <input
                  type={showPassword ? "text" : "password"}
                  id="password"
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline relative"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <button
                  className="absolute right-3 top-1/2 transform -translate-y-1/2"
                  onClick={handleShowPasswordToggle}
                >
                  {showPassword ? <FaEye /> : <FaEyeSlash />}
                </button>
              </div>
            </div>

            <div className="mb-4">
              <label
                htmlFor="rememberme"
                className="block text-gray-700 text-lg font-bold mb-2"
              >
                <input
                  className="text-sm"
                  type="checkbox"
                  id="rememberme"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                />

                <span className="ml-2 text-sm">Souvenez-vous de moi</span>
              </label>
            </div>
            <div className="mb-6">
              <button
                type="submit"
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
              >
                Login
              </button>

              <button className="py-2" onClick={handleForgetPasswordClick}>
                <a className="py-2"> • mot de passe oublié? cliquez ici</a>
              </button>
              <p className="py-2">
                • Vous n'avez pas encore vos identifiants ?
              </p>
              <button
                onClick={handleContactCLick}
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded focus:outline-none focus:shadow-outline "
              >
                Contactez nous
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default Login;
