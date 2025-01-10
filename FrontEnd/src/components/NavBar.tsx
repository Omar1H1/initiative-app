import { useState } from "react";
import { AiOutlineClose, AiOutlineMenu } from "react-icons/ai";
import { IoNotifications } from "react-icons/io5";
import { CiLogin, CiLogout } from "react-icons/ci";
import { useNavigate } from "react-router-dom";
import { useAtom, useAtomValue } from "jotai";
import loginAtom from "../service/LoginState";
import userAtom from "../service/UserAtom";
import ThemeToggle from "./ThemeToggle";
import useNotifications from "../hooks/useNotifications";
import logo from "../assets/logo.svg";

const Navbar = () => {
  const [nav, setNav] = useState(false);
  const navigate = useNavigate();
  const [token, setToken] = useAtom(loginAtom);
  const user = useAtomValue(userAtom);
  const notifications = useNotifications(
    user?.id ? String(user.id) : undefined,
  );

  const handleNavToggle = () => setNav((prevNav) => !prevNav);
  const handleLogout = () => {
    localStorage.removeItem("token");
    sessionStorage.removeItem("token");
    setToken(null);
    navigate("/");
  };

  const handleNotificationClick = () => {
    navigate("/natif");
    setNav(false);
  };

  const handleLogin = () => {
    navigate("/login");
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
              className="bg-blue-500 text-white p-4 hover:bg-blue-600 rounded-lg m-2 cursor-pointer"
            >
              Admin Panel
            </li>
          ) : null}
          <li
            onClick={handleLogout}
            className="bg-red-500 text-white p-4 hover:bg-red-600 rounded-lg m-2 cursor-pointer"
          >
            Logout
            <CiLogout className="inline-block ml-2" />
          </li>
        </>
      );
    } else {
      return (
        <li
          onClick={handleLogin}
          className="bg-green-500 text-white p-4 hover:bg-green-600 rounded-lg m-2 cursor-pointer"
        >
          Login
          <CiLogin className="inline-block ml-2" />
        </li>
      );
    }
  };

  return (
    <div className="bg-gray-800 fixed top-0 left-0 w-full z-50 flex justify-between items-center h-24 px-4 text-white shadow-md">
      {/* Logo */}
      <button onClick={() => navigate("/")}>
        <img src={logo} alt="logo" className="w-48 h-auto rounded-lg" />
      </button>

      {/* Desktop Navigation */}
      <ul className="hidden md:flex items-center">
        {token && (
          <li
            onClick={handleNotificationClick}
            className="cursor-pointer mr-4 relative"
          >
            <IoNotifications
              size={24}
              className="hover:bg-blue-400 hover:text-white rounded-full "
            />
            {notifications.length > 0 && (
              <span className="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full px-1">
                {notifications.length > 0 &&
                  (() => {
                    const newNotifications = notifications.filter(
                      (notification) => !notification.seen,
                    ).length;
                    return (
                      <span className="ml-2 bg-red-500 text-white text-xs rounded-full px-1">
                        {newNotifications}
                      </span>
                    );
                  })()}
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
      <div onClick={handleNavToggle} className="block md:hidden cursor-pointer">
        {nav ? <AiOutlineClose size={24} /> : <AiOutlineMenu size={24} />}
      </div>

      {/* Mobile Navigation */}
      <ul
        className={`fixed top-0 left-0 w-[60%] h-full border-r border-gray-700 bg-gray-900 transform ${nav ? "translate-x-0" : "-translate-x-full"
          } transition-transform duration-300 ease-in-out z-50 md:hidden`}
      >
        {/* Mobile Logo */}
        <div className="p-4">
          <img src={logo} alt="Logo" className="w-32 h-auto mx-auto" />
        </div>

        {/* Mobile Navigation Items */}

        {token && (
          <li className="m-4 cursor-pointer" onClick={handleNotificationClick}>
            <IoNotifications size={24} className="inline-block mr-2" />
            Notifications
            {notifications.length > 0 &&
              (() => {
                const newNotifications = notifications.filter(
                  (notification) => !notification.seen,
                ).length;
                return (
                  <span className="ml-2 bg-red-500 text-white text-xs rounded-full px-1">
                    {newNotifications}
                  </span>
                );
              })()}
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
