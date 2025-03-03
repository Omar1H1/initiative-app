import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./auth/Login.tsx";
import Create from "./auth/Create.tsx";
import NotFound from "./components/NotFound";
import NavBar from "./components/NavBar.tsx";
import CodePage from "./auth/CodePage.tsx";
import loginAtom from "./service/LoginState";
import { useSetAtom } from "jotai";
import ContactFrom from "./components/ContactFrom.tsx";
import PasswordRecovery from "./auth/PasswordRecovery.tsx";
import ResetPassword from "./auth/ResetPassword.tsx";
import Home from "./components/Home.tsx";
import { useEffect } from "react";
import AdminPanel from "./components/AdminPanel.tsx";
import Signup from "./auth/Signup.tsx";
import Notification from "./components/Notification.tsx";
import {Toaster} from "react-hot-toast";
import WebSocketTester from "./components/WebSocketTester.tsx";

const App = () => {
  const setToken = useSetAtom(loginAtom);

  useEffect(() => {
    const storedToken =
      localStorage.getItem("token") || sessionStorage.getItem("token") || null;
    setToken(storedToken);
  }, [setToken]);

  return (
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Create />} />
        <Route path="/" element={<Home />} />
        <Route path="/notification" element={<Notification />} />
        <Route path="/signup/submitcode" element={<CodePage />} />
        <Route path="/contact" element={<ContactFrom />} />
        <Route path="*" element={<NotFound />} />
        <Route path="/passwordrecovery" element={<PasswordRecovery />} />
        <Route path="/passwordreset" element={<ResetPassword />} />
        <Route path="/panel" element={<AdminPanel />} />
        <Route path="test" element={<Signup />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Toaster
          position="top-center"
          toastOptions={{
            className: 'bg-white text-black dark:bg-gray-800 dark:text-white',
            style: {
              border: '1px solid #D1D5DB',
              padding: '16px',
            }
          }}
      />
    </BrowserRouter>
  );
};

export default App;
