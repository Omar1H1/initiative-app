import {useState} from "react";
import {FaEye, FaEyeSlash} from "react-icons/fa";
import {Axios} from "../service/Axios.tsx";
import {useNavigate} from "react-router-dom";

const api = new Axios().getInstance();
const ResetPassword = () => {
    const queryParameters = new URLSearchParams(window.location.search);
    const pathCode: any = queryParameters.get("code");


    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const [passwordsMatch, setPasswordsMatch] = useState(false);

    const navigate = useNavigate();



    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();




        if(password !== confirmPassword) {
            setPasswordsMatch(true);
        } else {
            const data = {
                ActivationCode: pathCode,
                password,
            };

            try {
                const response = await api.post('/api/v1/users/resetpassword', data);
                console.log('User has been created with success:', response.data);
                navigate("/login")
            } catch (error) {
                console.error('Error during creation:', error);
            }

        }



    }

    const handleShowPasswordToggle = () => {
        setShowPassword((prev) => !prev);
    }

    const handleShowConfirmPasswordToggle = () => {
        setShowConfirmPassword((prev) => !prev);
    }

    return (
        <>
            <div className="container">
                    <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
                        <div
                            className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
                            <form onSubmit={handleSubmit}>
                                <div className="mb-4">
                                    {passwordsMatch ??
                                        <p className="text-2xl font-bold mb-4">vérifiez bien que les deux mots de passe
                                            sont
                                            identiques</p>}
                                    <label htmlFor="code" className="block text-gray-700 text-lg font-bold mb-2">
                                        Votre nouveau mot de pass
                                    </label>
                                    <div className="relative">
                                        <input
                                            type={showPassword ? "text" : "password"}
                                            id="password"
                                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline relative"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                        <button
                                            className="absolute right-3 top-1/2 transform -translate-y-1/2"
                                            onClick={handleShowPasswordToggle}
                                        >
                                            {showPassword ? <FaEye/> : <FaEyeSlash/>}
                                        </button>
                                    </div>
                                </div>
                                <div className="mb-4">
                                    <label htmlFor="code" className="block text-gray-700 text-lg font-bold mb-2">
                                        Confirmer votre mot de passe
                                    </label>
                                    <div className="relative">
                                        <input
                                            type={showConfirmPassword ? "text" : "password"}
                                            id="confirm-password"
                                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline relative"
                                            value={confirmPassword}
                                            onChange={(e) => setConfirmPassword(e.target.value)}
                                        />
                                        <button
                                            className="absolute right-3 top-1/2 transform -translate-y-1/2"
                                            onClick={handleShowConfirmPasswordToggle}
                                        >
                                            {showConfirmPassword ? <FaEye/> : <FaEyeSlash/>}
                                        </button>
                                    </div>
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
            </div>
        </>
)
}

export default ResetPassword;