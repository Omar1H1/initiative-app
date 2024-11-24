import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Hero = () => {
  const entrepreneursWords = ["porteurs", "entrepreneurs", "créateurs"];
  const mentorsWords = ["parrains", "mentors", "experts"];
  const [index, setIndex] = useState(0);
  const [fade, setFade] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(() => {
      setFade(false);
      setTimeout(() => {
        setIndex((prevIndex) => (prevIndex + 1) % entrepreneursWords.length);
        setFade(true);
      }, 500);
    }, 2500);
    return () => clearInterval(interval);
  }, [entrepreneursWords.length]);

  const handleClick = () => {
    navigate("/login");
  };

  return (
    <div>
      <div className="flex flex-col md:flex-row h-[600px] w-auto m-20 bg-gradient-to-b from-[#360033] to-[#0b8793] text-white p-20 rounded-lg">
        {/* Left Side - Text Content */}
        <div className="md:w-1/2 flex flex-col justify-center items-start p-4 text-center md:text-left p-15 relative">
          <h1 className="text-2xl font-extrabold mb-4 leading-25">
            Nous facilitons la mise en relation de nos{" "}
            <span
              className={` animate-pulse px-2 py-1 border-2 leading-20 rounded-lg border-blue-500 transition-opacity duration-500 ${fade ? "opacity-100" : "opacity-0"}`}
              style={{ backgroundColor: "rgba(29, 78, 216, 0.2)" }}
            >
              {entrepreneursWords[index]}
            </span>
            <br />
            avec des{" "}
            <span
              className={`animate-pulse px-2 py-1 border-2 leading-20 rounded-lg my-6 border-purple-500 transition-opacity duration-500 ${fade ? "opacity-100" : "opacity-0"}`}
              style={{ backgroundColor: "rgba(126, 34, 206, 0.2)" }}
            >
              {mentorsWords[index]}
            </span>
          </h1>
          <div className="flex justify-center md:justify-start gap-4">
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline absolute items-center"
              onClick={handleClick}
            >
              Commencer
            </button>
          </div>
        </div>

        <div className="hidden md:flex lg:flex justify-center items-center">
          <div className="grid grid-cols-2 gap-4 w-full max-w-md p-4">
            <img
              alt=""
              src="https://images.unsplash.com/photo-1485217988980-11786ced9454?ixlib=rb-4.0.3&amp;ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&amp;auto=format&amp;fit=crop&amp;h=528&amp;q=80"
              className="w-full h-40 object-cover rounded-lg shadow-lg"
              style={{ transform: "translateY(-10px)" }}
            />
            <img
              alt=""
              src="https://images.unsplash.com/photo-1559136555-9303baea8ebd?ixlib=rb-4.0.3&amp;ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&amp;auto=format&amp;fit=crop&amp;crop=focalpoint&amp;fp-x=.4&amp;w=396&amp;h=528&amp;q=80"
              className="w-full h-40 object-cover rounded-lg shadow-lg"
              style={{ transform: "translateY(15px)" }}
            />
            <img
              alt=""
              src="https://images.unsplash.com/photo-1670272504528-790c24957dda?ixlib=rb-4.0.3&amp;ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&amp;auto=format&amp;fit=crop&amp;crop=left&amp;w=400&amp;h=528&amp;q=80"
              className="w-full h-40 object-cover rounded-lg shadow-lg"
              style={{ transform: "translateY(10px)" }}
            />
            <img
              alt=""
              src="https://images.unsplash.com/photo-1670272505284-8faba1c31f7d?ixlib=rb-4.0.3&amp;ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&amp;auto=format&amp;fit=crop&amp;h=528&amp;q=80"
              className="w-full h-40 object-cover rounded-lg shadow-lg"
              style={{ transform: "translateY(25px)" }}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
