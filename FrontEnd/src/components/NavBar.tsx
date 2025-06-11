
import { useState } from "react";
import { AiOutlineClose, AiOutlineMenu } from "react-icons/ai";
import { IoNotifications } from "react-icons/io5";
import { CiLogin, CiLogout, CiUser } from "react-icons/ci";
import { useNavigate } from "react-router-dom";
import { useAtom, useAtomValue } from "jotai";
import loginAtom from "../service/LoginState";
import userAtom from "../service/UserAtom";
import ThemeToggle from "./ThemeToggle";
import useNotifications from "../hooks/useNotifications";
import logo from "../assets/logo.svg";
import { GrUserAdmin } from "react-icons/gr";
import { FaMessage } from "react-icons/fa6";

const Navbar = () => {
  const [nav, setNav] = useState(false);
  const navigate = useNavigate();
  const [token, setToken] = useAtom(loginAtom);
  const user = useAtomValue(userAtom);
  const { notifications } = useNotifications(
    user?.id ? String(user.id) : undefined,
  );

  const handleNavToggle = () => setNav((prevNav) => !prevNav);
  const handleLogout = () => {
    localStorage.removeItem("token");
    sessionStorage.removeItem("token");
    localStorage.removeItem("user");
    sessionStorage.removeItem("user");
    setToken(null);
    navigate("/");
  };

  const handleNotificationClick = () => {
    navigate("/notification");
    setNav(false);
  };

  const handleLogin = () => {
    navigate("/login");
    setNav(false);
  };

  const handleChatClick = () => {
    navigate("/chat");
    setNav(false);
  };

  const renderNavItems = () => {
    if (token) {
      return (
        <>
          {user?.role === "SUPERVISOR" || user?.role === "ADMIN" ? (
            <li
              onClick={() => {
                navigate("/panel");
                setNav(false);
              }}
              className="bg-blue-500 text-white font-bold p-4 hover:bg-blue-600 rounded-lg m-2 cursor-pointer"
            >
              Admin Panel
              <GrUserAdmin size={28} className="inline-block ml-2" />
            </li>
          ) : null}
          <li
            onClick={() => {
              navigate("/profile");
              setNav(false);
            }}
            className="bg-blue-500 text-white font-bold p-4 hover:bg-blue-600 rounded-lg m-2 cursor-pointer"
          >
            Profile
            <CiUser size={30} className="inline-block ml-2" />
          </li>
          <li
            onClick={handleLogout}
            className="bg-red-500 text-white font-bold p-4 hover:bg-red-600 rounded-lg m-2 cursor-pointer"
          >
            Déconnexion
            <CiLogout size={30} className="inline-block ml-2" />
          </li>
        </>
      );
    } else {
      return (
        <li
          onClick={handleLogin}
          className="bg-green-500 text-white p-4 hover:bg-green-600 rounded-lg m-2 cursor-pointer font-bold"
        >
          Se Connecter
          <CiLogin className="inline-block ml-2" />
        </li>
      );
    }
  };

  const newNotificationsCount = notifications.filter(
    (notification) => !notification.seen,
  ).length;

  return (
    <div className="bg-white dark:bg-gray-800 fixed top-0 left-0 w-full z-50 flex justify-between items-center h-24 px-4 text-white shadow-md">
      {/* Logo */}
      <button onClick={() => navigate("/")}>
        <img src={logo} alt="logo" className="w-48 h-auto rounded-lg" />
      </button>

      {/* Desktop Navigation */}
      <ul className="hidden md:flex items-center">
        {token && (
          // Chat Icon for Desktop
          <li
            onClick={handleChatClick}
            className="cursor-pointer mr-4 relative"
          >
            <FaMessage
              size={30}
              className="hover:bg-purple-400 hover:text-white rounded-full hover:rounded-sm text-black dark:text-gray-200 p-1"
            />
          </li>
        )}
        {token && (
          <li
            onClick={handleNotificationClick}
            className="cursor-pointer mr-4 relative"
          >
            <IoNotifications
              size={36}
              className="hover:bg-blue-400 hover:text-white rounded-full hover:rounded-sm text-black dark:text-gray-200"
            />
            {newNotificationsCount > 0 && (
              <span className="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full px-1">
                {newNotificationsCount}
              </span>
            )}
          </li>
        )}
        <li className="mr-4">
          <ThemeToggle />
        </li>
        {renderNavItems()}
      </ul>

      {/* Mobile Menu Icon */}
      <div
        onClick={handleNavToggle}
        className="block md:hidden cursor-pointer text-black dark:text-gray-200"
      >
        {nav ? <AiOutlineClose size={24} /> : <AiOutlineMenu size={24} />}
      </div>

      {/* Mobile Navigation */}
      <ul
        className={`fixed top-0 left-0 w-[60%] h-full border-r border-gray-700 bg-white dark:bg-gray-900 transform ${
          nav ? "translate-x-0" : "-translate-x-full"
        } transition-transform duration-300 ease-in-out z-50 md:hidden`}
      >
        {/* Mobile Logo */}
        <div className="p-4">
          <img src={logo} alt="Logo" className="w-32 h-auto mx-auto" />
        </div>

        {/* Mobile Navigation Items */}
        {token && (
          // Chat Icon for Mobile
          <li className="m-4 cursor-pointer" onClick={handleChatClick}>
            <FaMessage
              size={24}
              className="inline-block mr-2 text-black dark:text-gray-200"
            />
            <span className="text-black dark:text-gray-200">Chat</span>
          </li>
        )}
        {token && (
          <li className="m-4 cursor-pointer" onClick={handleNotificationClick}>
            <IoNotifications
              size={24}
              className="inline-block mr-2 text-black dark:text-gray-200"
            />
            <span className="text-black dark:text-gray-200">Notifications</span>
            {newNotificationsCount > 0 && (
              <span className="ml-2 bg-red-500 text-white text-xs rounded-full px-1">
                {newNotificationsCount}
              </span>
            )}
          </li>
        )}

        <li className="m-4">
          <ThemeToggle />
        </li>
        {renderNavItems()}
      </ul>
    </div>
  );
};

export default Navbar;

