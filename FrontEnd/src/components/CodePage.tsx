import { useState } from "react";
import Signup from "./Signup.tsx";
import {Axios} from "../service/Axios.tsx"

const api = new Axios().getInstance();

const CodePage = () => {
    const queryParameters = new URLSearchParams(window.location.search);
    const pathCode: any = queryParameters.get("code");
    const [code, setCode] = useState(pathCode);
    const [validCode, setValidCode] = useState(false);
    const [userInfo, setUserInfo] = useState({
        firstName: "",
        lastName: "",
        email: "",
        role: "",
    });

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const codeToActive = {
                activationCode: code,
            };

            const response = await api.post('/api/v1/users/preauthenticate', codeToActive);

            setUserInfo({
                firstName: response.data.firstName,
                lastName: response.data.lastName,
                email: response.data.email,
                role: response.data.role,
            });
            setValidCode(true);
            console.log("ok" + response.data.email);
            console.log(response.data.firstName);
        } catch (error) {
            console.error("Error during pre-registration:", error);
        }
    };

    return (
        <div className="container">
            {!validCode ? (
                <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
                    <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
                        <h2 className="text-2xl font-bold mb-6 text-center">Saisissez votre code</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-4">
                                <label htmlFor="code" className="block text-gray-700 text-lg font-bold mb-2">
                                    Code
                                </label>
                                <input
                                    type="text"
                                    id="code"
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    value={code}
                                    onChange={(e) => setCode(e.target.value)}
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
                    </div>
                </div>
            ) : (
                <Signup userInfo={userInfo} />
            )}
        </div>
    );
};

export default CodePage;