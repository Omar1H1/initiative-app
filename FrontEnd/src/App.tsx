import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Create from "./components/Create.tsx";
import Profile from "./components/Profile";
import NotFound from "./components/NotFound";
import NavBar from "./components/NavBar.tsx";
import CodePage from "./components/CodePage.tsx";
import IsLogged from "./service/LoginState.tsx";
import { useState } from "react";
import Hero from "./components/Hero.tsx";
import ContactFrom from "./components/ContactFrom.tsx";
import PasswordRecovery from "./components/PasswordRecovery.tsx";
import ResetPassword from "./components/ResetPassword.tsx";

const App = () => {
  const [token, setToken] = useState(
    localStorage.getItem("token") ?? sessionStorage.getItem("token") ?? "",
  );

  const value = [token, setToken];

  return (
    // @ts-ignore
    <IsLogged.Provider value={value}>
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route path="/" element={<Hero />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Create />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/signup/submitcode" element={<CodePage />} />
          <Route path="/contact" element={<ContactFrom />} />
          <Route path="*" element={<NotFound />} />
          <Route path="/passwordrecovery" element={<PasswordRecovery />} />
          <Route path="/passwordreset" element={<ResetPassword />} />
        </Routes>
      </BrowserRouter>
    </IsLogged.Provider>
  );
};

export default App;
