import { useState } from "react";
import { Axios } from "../service/Axios.tsx";

const api = new Axios().getInstance();

const ContactFrom = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [text, setText] = useState("");

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    const data = {
      firstName,
      lastName,
      email,
      text,
    };

    try {
      const response = await api.post("/api/v1/contact", data);
      console.log("Contact form has been sent with success : ", response.data);
    } catch (error) {
      console.log("error happend during the submit opreation : ", error);
    }
  };

  return (
    <>
      <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
        <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
          <h2 className="text-2xl font-bold mb-6 text-center">
            Contactez-nous
          </h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label
                htmlFor="firstName"
                className="block text-gray-700 text-lg font-bold mb-2"
              >
                Prénom
              </label>
              <input
                type="text"
                id="firstName"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
              />
            </div>
            <div className="mb-4">
              <label
                htmlFor="lastName"
                className="block text-gray-700 text-lg font-bold mb-2"
              >
                Nom
              </label>
              <input
                type="text"
                id="lastName"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
              />
            </div>
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
            <div className="mb-4">
              <label
                htmlFor="textarea"
                className="block text-gray-700 text-sm font-bold mb-2"
              >
                Pouvez vous dans 250 caracteres expliquer votre projet
              </label>
              <textarea
                className="lg:max-w-full sm:w-fit sm:max-w-[270px] rounded-lg shadow-md border border-gray-300 p-2 w-fit"
                id="text-area"
                rows="4"
                cols="50"
                maxLength="280"
                value={text}
                onChange={(e) => setText(e.target.value)}
              ></textarea>
            </div>

            <div className="mb-6">
              <button
                type="submit"
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
              >
                Envoyer
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default ContactFrom;
