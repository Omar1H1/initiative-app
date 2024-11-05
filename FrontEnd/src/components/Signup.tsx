import { useState } from "react";
import axios from "axios";

const Signup = (props: any) => {
    const { userInfo } = props;


    const [firstName, setFirstName] = useState(userInfo.firstName || '');
    const [lastName, setLastName] = useState(userInfo.lastName || '');
    const [email, setEmail] = useState(userInfo.email || '');
    const [password, setPassword] = useState('');
    const [username, setUsername] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const handleSubmit = async (e: any) => {
        e.preventDefault();

        const data = {
            username,
            firstName,
            lastName,
            email,
            password,
        };

        try {
            const response = await axios.post('http://localhost:8080/api/v1/users/register', data);
            console.log('User has been created with success:', response.data);
            if(rememberMe) {
                localStorage.setItem("token", response.data.token)
            } else {
                sessionStorage.setItem("token", response.data.token)
            }
        } catch (error) {
            console.error('Error during creation:', error);
        }
    };

    return (
        <>
            <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
                <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
                    <h2 className="text-2xl font-bold mb-6 text-center">Créer une compte</h2>
                    <form onSubmit={handleSubmit}>
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

                        <div className="mb-4">
                            <label htmlFor="password" className="block text-gray-700 text-lg font-bold mb-2">
                                Password
                            </label>
                            <input
                                type="password"
                                id="password"
                                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="username" className="block text-gray-700 text-lg font-bold mb-2">
                                username
                            </label>
                            <input
                                type="text"
                                id="username"
                                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                        <div className="mb-4">
                            <label htmlFor="rememberme" className="block text-gray-700 text-lg font-bold mb-2">
                                Remember Me
                            </label>
                            <input
                                type="checkbox"
                                id="rememberme"

                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                        </div>
                        <div className="mb-6">
                            <button
                                type="submit"
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                            >
                                Sign Up
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};

export default Signup;