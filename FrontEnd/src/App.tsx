import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./components/Home";
import Login from "./components/Login";
import Create from "./components/Create.tsx";
import Profile from "./components/Profile";
import NotFound from "./components/NotFound";
import NavBar from "./components/NavBar.tsx";
import CodePage from "./components/CodePage.tsx";

function App() {
  return (
      <BrowserRouter>
          <NavBar />
        <div className="absolute inset-0 -z-10 h-full w-full items-center px-5 py-24 [background:radial-gradient(125%_125%_at_50%_10%,#000_40%,#63e_100%)]">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Create />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/signup/submitcode" element={<CodePage />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </div>
      </BrowserRouter>
  );
}

export default App;