import { useState } from "react";
import { Axios } from "../service/Axios";

const api = new Axios().getInstance();

const PasswordRecovery = () => {
  const [email, setEmail] = useState("");
  const [isValidEmail, setIsValidEmail] = useState(false);
  const [isError, setIsError] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = {
      email,
    };

    try {
      const response = await api.post("/api/v1/users/forgetpassword", data);
      console.log(response.data);
      setIsValidEmail(true);
      setIsError(false);
    } catch (error) {
      console.log(error);
      setIsError(true);
    }
  };

  return (
    <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
      <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
        {!isError ? (
          !isValidEmail ? (
            <>
              <h2 className="text-2xl font-bold mb-6 text-center">
                Saisissez votre email
              </h2>
              <form onSubmit={handleSubmit}>
                <div className="mb-4">
                  <label
                    htmlFor="email"
                    className="block text-gray-700 text-lg font-bold mb-2"
                  >
                    Email
                  </label>
                  <input
                    type="email"
                    id="email"
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                </div>
                <div className="mb-6">
                  <button
                    type="submit"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                  >
                    envoyer
                  </button>
                </div>
              </form>
            </>
          ) : (
            <p className="text-center">
              Un lien pour changer votre mot de passe vous a été envoyé sur
              votre adresse mail : <span className="font-bold">{email}</span>
            </p>
          )
        ) : (
          <p>L'email renseigné n'existait pas</p>
        )}
      </div>
    </div>
  );
};

export default PasswordRecovery;
