// @ts-ignore
import React, { useState } from "react";
import { Axios } from "../service/Axios.tsx";
import { useNavigate } from "react-router-dom";
import { useAtomValue } from "jotai";
import userAtom from "../service/UserAtom.tsx";
import { toast } from "react-hot-toast";

const api = new Axios().getInstance();

const allRoles = [
  { value: "ADMIN", label: "Admin" },
  { value: "SUPERVISOR", label: "Supervisor" },
  { value: "PARRAIN", label: "Parrain" },
  { value: "PORTEUR", label: "Porteur" },
];

const Create = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("PORTEUR");
  const navigate = useNavigate();
  const user = useAtomValue(userAtom);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = {
      firstName,
      lastName,
      email,
      role,
    };

    toast.promise(
        api.post("/api/v1/users/create", data),
        {
          loading: 'Creating...',
          success: <b>L'utilisateur est bien initialisé!</b>,
          error: <b>Error during pre-registration.</b>,
        }
    ).then(() => {
      navigate("/");
    }).catch((error) => {
      console.error("Error during pre-registration:", error);
    });
  };

  const filteredRoles =
      user?.role === "ADMIN"
          ? allRoles
          : user?.role === "SUPERVISOR"
              ? allRoles.filter(
                  (role) => role.value === "PARRAIN" || role.value === "PORTEUR",
              )
              : [];

  return (
      <div className="bg-bkg h-screen flex items-center justify-center px-4 py-8 dark:bg-gray-900">
        <div className="w-full max-w-lg bg-white bg-opacity-90 backdrop-filter backdrop-blur-lg rounded-lg shadow-lg p-8 dark:bg-gray-800 dark:bg-opacity-90">
          <h2 className="text-lg sm:text-xl md:text-2xl lg:text-3xl font-bold text-gray-800 mb-6 text-center dark:text-white">
            Créer un compte
          </h2>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label
                  htmlFor="firstName"
                  className="block text-gray-700 font-bold mb-2 dark:text-gray-300"
              >
                Prénom
              </label>
              <input
                  type="text"
                  id="firstName"
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
              />
            </div>
            <div>
              <label
                  htmlFor="lastName"
                  className="block text-gray-700 font-bold mb-2 dark:text-gray-300"
              >
                Nom
              </label>
              <input
                  type="text"
                  id="lastName"
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
              />
            </div>
            <div>
              <label
                  htmlFor="email"
                  className="block text-gray-700 font-bold mb-2 dark:text-gray-300"
              >
                Email
              </label>
              <input
                  type="email"
                  id="email"
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div>
              <label
                  htmlFor="role"
                  className="block text-gray-700 font-bold mb-2 dark:text-gray-300"
              >
                Rôle
              </label>
              <select
                  id="role"
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
              >
                {filteredRoles.map((role) => (
                    <option key={role.value} value={role.value}>
                      {role.label}
                    </option>
                ))}
              </select>
            </div>
            <div>
              <button
                  type="submit"
                  className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring focus:ring-blue-300 w-full"
                  name="signup"
                  role="button"
              >
                Inscrire
              </button>
            </div>
          </form>
        </div>
      </div>
  );
};

export default Create;