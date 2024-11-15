import { useState, useContext } from "react";
import { AiOutlineClose, AiOutlineMenu } from "react-icons/ai";
import { _, useNavigate } from "react-router-dom";
import logo from "../assets/logo.svg";
import isLogged from "../service/LoginState.tsx";
import { CiLogin, CiLogout } from "react-icons/ci";

const Navbar = () => {
  const [nav, setNav] = useState(false);
  const navigate = useNavigate();
  const [token, setToken] = useContext(isLogged);

  const handleNav = () => {
    setNav(!nav);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    sessionStorage.removeItem("token");
    setToken(null);
    navigate("/");
  };

  const handleLogin = () => {
    navigate("/login");
  };

  const handleLogoCLick = () => {
    navigate("/");
  };

  return (
    <div className="bg-transparent flex justify-between items-center h-24 max-w-[1240px] mx-auto px-4 text-white">
      <button onClick={handleLogoCLick}>
        <img
          src={logo}
          alt="logo"
          className="w-48 h-auto bg-transparent rounded-lg"
        />
      </button>
      {token ? (
        <ul className="hidden md:flex">
          <div className="relative">
            <li
              onClick={handleLogout}
              className="p-4 hover:bg-[#ffc0cb] rounded-xl m-2 cursor-pointer duration-300 hover:text-black"
            >
              Logout
            </li>
            <CiLogout className="absolute size-6  ml-16 top-1/2 transform -translate-y-1/2" />
          </div>
        </ul>
      ) : (
        <ul className="hidden md:flex">
          <div className="relative">
            <li
              onClick={handleLogin}
              className="bg-blue-400 p-4 hover:bg-blue-700 rounded-xl m-2 cursor-pointer duration-300 hover:text-white w-20"
            >
              Login
            </li>
            <CiLogin className="absolute size-6  ml-16 top-1/2 transform -translate-y-1/2" />
          </div>
        </ul>
      )}

      <div onClick={handleNav} className="block md:hidden">
        {nav ? <AiOutlineClose size={20} /> : <AiOutlineMenu size={20} />}
      </div>

      <ul
        className={
          nav
            ? "fixed md:hidden left-0 top-0 w-[60%] h-full border-r border-r-gray-900 bg-[#000300] ease-in-out duration-500"
            : "ease-in-out w-[60%] duration-500 fixed top-0 bottom-0 left-[-100%]"
        }
      >
        <img
          src={logo}
          alt="Your Logo"
          className="w-48 h-auto bg-transparent"
        />
        {token ? (
          <li
            onClick={handleLogout}
            className="p-4 border-b rounded-xl hover:bg-[#ffc0cb] duration-300 hover:text-black cursor-pointer border-gray-600"
          >
            Logout
          </li>
        ) : (
          <li
            onClick={handleLogin}
            className="p-4 border-b rounded-xl hover:bg-[#ffc0cb] duration-300 hover:text-black cursor-pointer border-gray-600"
          >
            Login
          </li>
        )}
      </ul>
    </div>
  );
};

export default Navbar;
