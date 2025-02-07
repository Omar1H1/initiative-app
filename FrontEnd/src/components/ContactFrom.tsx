import { useState } from "react";
import { Axios } from "../service/Axios.tsx";
import {useNavigate} from "react-router-dom";
import {toast} from "react-hot-toast";

const api = new Axios().getInstance();

const ContactFrom = () => {
  const [firstname, setFirstName] = useState("");
  const [lastname, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [project, setProject] = useState("");
  const navigate = useNavigate();


  const handleSubmit = async (e: any) => {
    e.preventDefault();

    const data = {
      firstname,
      lastname,
      email,
      project,
    };

    toast.promise(
        api.post("/api/v1/contact", data),
        {
          loading: 'Envoi en cours...',
          success: <b>Le formulaire de contact a été envoyé avec succès !</b>,
          error: <b>Une erreur est survenue lors de l'envoi du formulaire de contact.</b>,
        }
    ).then(() => {
      console.log("Contact form has been sent with success");
      navigate("/");
    }).catch((error) => {
      console.error("Error during the submit operation: ", error);
    });
  };


  // @ts-ignore
  return (
    <>
      <div className="bg-transparent h-screen flex items-center justify-center rounded-lg overflow-hidden">
        <div className="dark:bg-gray-800 bg-white w-11/12 sm:w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50 overflow-hidden">
          <h2 className="text-2xl font-bold mb-6 text-center dark:text-white">
            Contactez-nous
          </h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label
                htmlFor="firstName"
                className="block text-gray-700 text-lg font-bold mb-2 dark:text-white"
              >
                Prénom
              </label>
              <input
                type="text"
                id="firstName"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                value={firstname}
                onChange={(e) => setFirstName(e.target.value)}
              />
            </div>
            <div className="mb-4">
              <label
                htmlFor="lastName"
                className="block text-gray-700 text-lg font-bold mb-2 dark:text-white"
              >
                Nom
              </label>
              <input
                type="text"
                id="lastName"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                value={lastname}
                onChange={(e) => setLastName(e.target.value)}
              />
            </div>
            <div className="mb-4">
              <label
                htmlFor="email"
                className="block text-gray-700 text-lg font-bold mb-2 dark:text-white"
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
                className="block text-gray-700 text-sm font-bold mb-2 dark:text-white"
              >
                Pouvez vous dans 250 caracteres expliquer votre projet
              </label>
              <div className="lg:max-w-full sm:w-full sm:max-w-[170px] rounded-lg shadow-md border border-gray-300 p-2 w-full text-black">
       <textarea
      className="w-full h-32 p-2 border border-gray-400 rounded-lg resize-none"                id="text-area"
                rows="4"
                cols="50"
                maxLength="280"
                value={project}
                onChange={(e) => setProject(e.target.value)}
               ></textarea>
              </div>
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
