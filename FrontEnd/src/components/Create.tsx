import { useState } from 'react';
import axios from 'axios';

const roles = [
  { value: 'ADMIN', label: 'Admin' },
  { value: 'SUPERVISOR', label: 'Supervisor' },
  { value: 'PARRAIN', label: 'Parrain' },
  { value: 'PORTEUR', label: 'Porteur' },
];

const Create = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('PORTEUR');

  const handleSubmit = async (e : any) => {
    e.preventDefault();

    const data = {
      firstName,
      lastName,
      email,
      role,
    };

    try {
      const response = await axios.post('http://localhost:8080/api/v1/users/create', data);
      console.log('Pre-registration successful:', response.data);
    } catch (error) {
      console.error('Error during pre-registration:', error);
    }
  };

  return (
      <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
        <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
          <h2 className="text-2xl font-bold mb-6 text-center">Créer une compte</h2>
          <div className="mb-4">
            <label htmlFor="firstName" className="block text-gray-700 text-lg font-bold mb-2">
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
            <label htmlFor="lastName" className="block text-gray-700 text-lg font-bold mb-2">
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
            <label htmlFor="email" className="block text-gray-700 text-lg font-bold mb-2">
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
          <div className="mb-6 rounded-lg text-2xl">
            <label htmlFor="role" className="block text-gray-700 text-lg font-bold mb-2">
              Role
            </label>
            <select id="role" value={role} onChange={(e) => setRole(e.target.value)}>
              {roles.map((role) => (
                  <option key={role.value} value={role.value}>
                    {role.label}
                  </option>
              ))}
            </select>
          </div>
          <div className="mb-6">
            <button
                type="submit"
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                onClick={handleSubmit}
            >
              Sign Up
            </button>
          </div>
        </div>
      </div>
  );
};

export default Create;