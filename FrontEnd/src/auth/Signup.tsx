import { useState } from "react";
import { Axios } from "../service/Axios.tsx";
import { useNavigate } from "react-router-dom";
import MultiSelect from "../components/MultiSelect.tsx";
import { FaEye, FaEyeSlash } from "react-icons/fa";

const api = new Axios().getInstance();

const Signup = (props: any) => {
    const { userInfo } = props;
    const navigate = useNavigate();

    const [firstName, setFirstName] = useState(userInfo.firstName || "");
    const [lastName, setLastName] = useState(userInfo.lastName || "");
    const [email, setEmail] = useState(userInfo.email || "");
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [passwordsMatch, setPasswordsMatch] = useState(false);
    const [username, setUsername] = useState("");
    const [rememberMe, setRememberMe] = useState(false);
    const [selectedImage, setSelectedImage] = useState<File | null>(null);
    const [selectedOptions, setSelectedOptions] = useState<string[]>([]);
    const [projectDescription, setProjectDescription] = useState("");

    const handleShowPasswordToggle = () => {
        setShowPassword((prev) => !prev);
    };

    const handleShowConfirmPasswordToggle = () => {
        setShowConfirmPassword((prev) => !prev);
    };

    const handleSubmit = async (e: any) => {
        e.preventDefault();

        if (!selectedImage) {
            alert("Please upload a profile image.");
            return;
        }

        if (password !== confirmPassword) {
            setPasswordsMatch(true);
            return;
        } else {
            setPasswordsMatch(false);
        }

        const formData = new FormData();
        const userInfo = {
            username,
            firstName,
            lastName,
            email,
            password,
            projectDescription,
            sectorOfActivity: selectedOptions[0] || "",
        };

        console.log("User  Info:", userInfo);

        formData.append("request", new Blob([JSON.stringify(userInfo)], { type: "application/json" }));
        formData.append("profileImage", selectedImage);

        try {
            const response = await api.post("/api/v1/users/register", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            console.log("User  has been created successfully:", response.data);
            navigate("/login");
        } catch (error) {
            console.error("Error during creation:", error);
        }
    };

    return (
        <div className="min-h-screen px-4 flex items-center justify-center bg-white dark:bg-gray-800">
            <div className="w-full max-w-lg bg-white dark:bg-gray-800 rounded-lg shadow-lg p-8 mt-24">
                <h2 className="text-lg sm:text-xl md:text-2xl lg:text-3xl font-bold text-gray-800 dark:text-white mb-6 text-center">
                    Créer un compte
                </h2>
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="firstName" className="block text-gray-700 dark:text-gray-300 font-bold mb-2">Prénom</label>
                        <input
                            type="text"
                            id="firstName"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 dark:text-white dark:bg-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="lastName" className="block text-gray-700 dark:text-gray-300 font-bold mb-2">Nom</label>
                        <input
                            type="text"
                            id="lastName"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 dark:text-white dark:bg-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="email" className="block text-gray-700 dark:text-gray-300 font-bold mb-2">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 dark:text-white dark:bg-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>

                    <div className="mb-4">
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700 dark:text-gray-300 pb-3">Mot de passe</label>
                        <div className="relative">
                            <input
                                type={showPassword ? "text" : "password"}
                                id="password"
                                className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-800 leading-tight focus:outline-none focus:ring ${
                                    passwordsMatch ? "border-red-500 dark:border-red-700" : "border-gray-300"
                                } dark:border-gray-600 dark:bg-gray-700 dark:text-white`}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300"
                                onClick={handleShowPasswordToggle}
                            >
                                {showPassword ? <FaEye /> : <FaEyeSlash />}
                            </button>
                        </div>
                    </div>

                    <div className="mb-4">
                        <label htmlFor="confirm-password" className="block text-sm font-medium text-gray-700 dark:text-gray-300 pb-3">Confirmer votre mot de passe</label>
                        <div className="relative">
                            <input
                                type={showConfirmPassword ? "text" : "password"}
                                id="confirm-password"
                                className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-800 leading-tight focus:outline-none focus:ring ${
                                    passwordsMatch ? "border-red-500 dark:border-red-700": "border-gray-300"
                                } dark:border-gray-600 dark:bg-gray-700 dark:text-white`}
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 dark:text-gray-300"
                                onClick={handleShowConfirmPasswordToggle}
                            >
                                {showConfirmPassword ? <FaEye /> : <FaEyeSlash />}
                            </button>
                        </div>
                    </div>

                    <div>
                        <label htmlFor="username" className="block text-gray-700 dark:text-gray-300 font-bold mb-2">Nom d'utilisateur</label>
                        <input
                            type="text"
                            id="username"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 dark:text-white dark:bg-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="profileImage" className="block text-gray-700 dark:text-gray-300 font-bold mb-2">Image de profil</label>
                        <input
                            type="file"
                            id="profileImage"
                            accept="image/*"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 dark:text-white dark:bg-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            onChange={(e) => {
                                if (e.target.files && e.target.files[0]) {
                                    setSelectedImage(e.target.files[0]);
                                }
                            }}
                            required
                        />
                    </div>

                    <div>
                        <label htmlFor="projectDescription" className="block text-gray-700 dark:text-gray-300 font-bold mb-2">Pouvez vous dans 250 caracteres expliquer votre projet ou bien domain d'expertise</label>
                        <textarea
                            className="w-full h-32 p-2 border border-gray-400 rounded-lg resize-none dark:bg-gray-700 dark:text-white"
                            id="projectDescription"
                            rows="4"
                            cols="50"
                            maxLength="280"
                            value={projectDescription}
                            onChange={(e) => setProjectDescription(e.target.value)}
                        ></textarea>
                    </div>

                    <MultiSelect onChange={setSelectedOptions} />

                    <div className="flex items-center">
                        <input
                            type="checkbox"
                            id="rememberme"
                            checked={rememberMe}
                            onChange={(e) => setRememberMe(e.target.checked)}
                            className="w-4 h-4 border-gray-300 rounded"
                        />
                        <label htmlFor="rememberme" className="ml-2 text-gray-700 dark:text-gray-300">Se souvenir de moi</label>
                    </div>

                    <button
                        type="submit"
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-full focus:outline-none focus:ring focus:ring-blue-300"
                    >
                        S'inscrire
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Signup;