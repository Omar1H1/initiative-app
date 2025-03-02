import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Hero = () => {
  const entrepreneursWords = ["Porteurs", "Entrepreneurs", "Créateurs"];
  const mentorsWords = ["Parrains", "Mentors", "Experts"];
  const [index, setIndex] = useState(0);
  const [fade, setFade] = useState(true);
  const [imageOrder, setImageOrder] = useState([0, 1, 2, 3]);
  const navigate = useNavigate();

  const images = [
    "https://images.unsplash.com/photo-1485217988980-11786ced9454?ixlib=rb-4.0.3&auto=format&fit=crop&h=528&q=80",
    "https://images.unsplash.com/photo-1559136555-9303baea8ebd?ixlib=rb-4.0.3&auto=format&fit=crop&crop=focalpoint&fp-x=.4&w=396&h=528&q=80",
    "https://images.unsplash.com/photo-1670272504528-790c24957dda?ixlib=rb-4.0.3&auto=format&fit=crop&crop=left&w=400&h=528&q=80",
    "https://images.unsplash.com/photo-1670272505284-8faba1c31f7d?ixlib=rb-4.0.3&auto=format&fit=crop&h=528&q=80",
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setFade(false);
      setTimeout(() => {
        setIndex((prevIndex) => (prevIndex + 1) % entrepreneursWords.length);
        setImageOrder((prevOrder) => {
          const newOrder = [...prevOrder];
          const first = newOrder.shift();
          if (first != null) {
            newOrder.push(first);
          }
          return newOrder;
        });
        setFade(true);
      }, 500);
    }, 2500);
    return () => clearInterval(interval);
  }, [entrepreneursWords.length]);

  const handleClick = () => {
    navigate("/login");
  };

  return (
    <div className="flex flex-col items-center sm:items-stretch mt-60 mx-20 dark:bg-gray-900">
      <div className="flex flex-col md:flex-row min-h-min md:h-[600px] w-full px-6 py-12 md:px-20 bg-gradient-to-b from-[#360033] to-[#0b8793] text-white rounded-lg md:rounded-3xl overflow-hidden">
        {/* Left Side - Text Content */}
        <div className="md:w-1/2 flex flex-col justify-center items-center md:items-start text-center md:text-left space-y-6">
          <h1 className="text-3xl sm:text-4xl md:text-5xl font-extrabold leading-relaxed">
            Nous facilitons la mise en relation des{" "}
            <span
              className={`inline-block px-1 py-1 border-2 border-spacing-4 rounded-lg border-blue-500 transition-opacity duration-500 ${fade ? "opacity-100" : "opacity-0"}`}
              style={{
                display: "inline-block",
                minWidth: "120px",
                textAlign: "center",
                backgroundColor: "rgba(29, 78, 216, 0.2)",
              }}
            >
              {entrepreneursWords[index]}
            </span>{" "}
            <br />
            avec des{" "}
            <span
              className={`inline-block px-2 py-1 border-2 mt-2 rounded-lg border-purple-500 transition-opacity duration-500 ${fade ? "opacity-100" : "opacity-0"}`}
              style={{
                display: "inline-block",
                minWidth: "120px",
                textAlign: "center",
                backgroundColor: "rgba(126, 34, 206, 0.2)",
              }}
            >
              {mentorsWords[index]}
            </span>
          </h1>
          <div className="relative w-full mt-4 flex justify-center">
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded focus:outline-none focus:shadow-outline"
              onClick={handleClick}
            >
              Commencer
            </button>
          </div>
        </div>

        {/* Right Side - Image Content */}
        <div className="flex justify-center items-center md:w-1/2 mt-10 md:mt-0">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full max-w-lg">
            {imageOrder.map((orderIdx, idx) => (
              <div
                key={idx}
                className={`relative w-full h-40 transition-all duration-1000 ease-linear transform ${fade ? "opacity-100 scale-100" : "opacity-0 scale-95"}`}
              >
                <img
                  alt=""
                  src={images[orderIdx]}
                  className="w-full h-full object-cover rounded-lg shadow-lg"
                />
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
