import { useState, useEffect } from 'react';
import { AiOutlineClose, AiOutlineMenu } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import logo from "../assets/logo.png";

const Navbar = () => {
    const [nav, setNav] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();


    useEffect(() => {
        const token = localStorage.getItem("token") ?? sessionStorage.getItem("token");
        setIsLoggedIn(token !== "");
    }, []);

    const handleNav = () => {
        setNav(!nav);
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        sessionStorage.removeItem("token");
        setIsLoggedIn(false);
        navigate("/");

    };

    const handleLogin = () => {
        navigate("/login");
    };

    return (
        <div className='bg-transparent flex justify-between items-center h-24 max-w-[1240px] mx-auto px-4 text-white'>
            <img src={logo} alt="logo"
                 className="w-48 h-auto bg-transparent bg-gradient-to-r from-indigo-500 from-10% via-sky-500 via-30% to-emerald-500 to-90% rounded-lg"/>

            {isLoggedIn ? (
                <ul className='hidden md:flex'>
                    <li
                        onClick={handleLogout}
                        className='p-4 hover:bg-[#ffc0cb] rounded-xl m-2 cursor-pointer duration-300 hover:text-black'
                    >
                        Logout
                    </li>
                </ul>
            ) : (
                <ul className='hidden md:flex'>
                    <li
                        onClick={handleLogin}
                        className='p-4 hover:bg-[#ffc0cb] rounded-xl m-2 cursor-pointer duration-300 hover:text-black'
                    >
                        Login
                    </li>
                </ul>
            )}

            <div onClick={handleNav} className='block md:hidden'>
                {nav ? <AiOutlineClose size={20}/> : <AiOutlineMenu size={20}/>}
            </div>

            <ul
                className={
                    nav
                        ? 'fixed md:hidden left-0 top-0 w-[60%] h-full border-r border-r-gray-900 bg-[#000300] ease-in-out duration-500'
                        : 'ease-in-out w-[60%] duration-500 fixed top-0 bottom-0 left-[-100%]'
                }
            >
                <img src={logo} alt="Your Logo" className="w-48 h-auto bg-transparent"/>
                {isLoggedIn ? (
                    <li
                        onClick={handleLogout}
                        className='p-4 border-b rounded-xl hover:bg-[#ffc0cb] duration-300 hover:text-black cursor-pointer border-gray-600'
                    >
                        Logout
                    </li>
                ) : (
                    <li
                        onClick={handleLogin}
                        className='p-4 border-b rounded-xl hover:bg-[#ffc0cb] duration-300 hover:text-black cursor-pointer border-gray-600'
                    >
                        Login
                    </li>
                )}
            </ul>
        </div>
    );
};

export default Navbar;