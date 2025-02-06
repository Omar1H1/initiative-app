import { useState } from "react";
import { Axios } from "../service/Axios.tsx";
import { useNavigate } from "react-router-dom";
import MultiSelect from "../components/MultiSelect.tsx";

const api = new Axios().getInstance();

const Signup = (props: any) => {
    const { userInfo } = props;
    const navigate = useNavigate();

    const [firstName, setFirstName] = useState(userInfo.firstName || "");
    const [lastName, setLastName] = useState(userInfo.lastName || "");
    const [email, setEmail] = useState(userInfo.email || "");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [rememberMe, setRememberMe] = useState(false);
    const [selectedImage, setSelectedImage] = useState<File | null>(null);
    const [selectedOptions, setSelectedOptions] = useState<string[]>([]);

    const handleSubmit = async (e: any) => {
        e.preventDefault();

        if (!selectedImage) {
            alert("Please upload a profile image.");
            return;
        }

        const formData = new FormData();
        const userInfo = {
            username,
            firstName,
            lastName,
            email,
            password,
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
        <div className="bg-gradient-to-b from-[#360033] to-[#0b8793] min-h-screen px-4 flex items-center justify-center">
            <div className="w-full max-w-lg bg-white bg-opacity-90 backdrop-filter backdrop-blur-lg rounded-lg shadow-lg p-8 mt-24">
                <h2 className="text-lg sm:text-xl md:text-2xl lg:text-3xl font-bold text-gray-800 mb-6 text-center">
                    Créer un compte
                </h2>
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="firstName" className="block text-gray-700 font-bold mb-2">Prénom</label>
                        <input
                            type="text"
                            id="firstName"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="lastName" className="block text-gray-700 font-bold mb-2">Nom</label>
                        <input
                            type="text"
                            id="lastName"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="email" className="block text-gray-700 font-bold mb-2">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-gray-700 font-bold mb-2">Mot de passe</label>
                        <input
                            type="password"
                            id="password"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="username" className="block text-gray-700 font-bold mb-2">Nom d'utilisateur</label>
                        <input
                            type="text"
                            id="username"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>

                    <div>
                        <label htmlFor="profileImage" className="block text-gray-700 font-bold mb-2">Image de profil</label>
                        <input
                            type="file"
                            id="profileImage"
                            accept="image/*"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500"
                            onChange={(e) => {
                                if (e.target.files && e.target.files[0]) {
                                    setSelectedImage(e.target.files[0]);
                                }
                            }}
                            required
                        />
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
                        <label htmlFor="rememberme" className="ml-2 text-gray-700">Se souvenir de moi</label>
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