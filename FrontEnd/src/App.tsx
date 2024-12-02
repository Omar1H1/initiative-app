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
        <Route path="/signup/submitcode" element={<CodePage />} />
        <Route path="/contact" element={<ContactFrom />} />
        <Route path="*" element={<NotFound />} />
        <Route path="/passwordrecovery" element={<PasswordRecovery />} />
        <Route path="/passwordreset" element={<ResetPassword />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
