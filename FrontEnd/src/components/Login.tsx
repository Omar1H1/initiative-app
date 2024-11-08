import {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Axios} from "../service/Axios.tsx"
import isLogged from "../service/LoginState.tsx";


const api = new Axios().getInstance();

const Login = () => {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();
  const [_, setToken] = useContext(isLogged);


  const handleSubmit = async (e: any) => {
    e.preventDefault();

    const data = {
      email,
      password,
    };

    try {
      const response = await api.post('/api/v1/users/authenticate', data);
      console.log('User has been created with success:', response.data);
      if(rememberMe) {
        localStorage.setItem("token", response.data.token)
        setToken(response.data.token)
      } else {
        sessionStorage.setItem("token", response.data.token)
        setToken(response.data.token)
      }
      navigate("/")

    } catch (error) {
      console.error('Error during creation:', error);
    }
  };

  return (
      <>
        <div className="bg-transparent h-screen flex items-center justify-center rounded-lg">
          <div className="bg-transparent bg-gradient-to-r from-cyan-300 to-blue-100 w-4/12 shadow-md px-8 pt-6 pb-8 mb-4 rounded-lg bg-opacity-90 backdrop-filter backdrop-blur-lg backdrop-brightness-50">
            <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
            <form onSubmit={handleSubmit}>
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
                  mot de passe
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
                <label htmlFor="rememberme" className="block text-gray-700 text-lg font-bold mb-2">
                  <input
                      className="text-sm"
                      type="checkbox"
                      id="rememberme"

                      checked={rememberMe}
                      onChange={(e) => setRememberMe(e.target.checked)}
                  />
                  <span className="ml-2 text-sm">Souvenez-vous de moi</span>

                </label>
              </div>

              <div className="mb-6">
                <button
                    type="submit"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                >
                  Login
                </button>
                <p className="py-6"
                >• Vous n'avez pas encore vos identifiants ?</p>
                <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded focus:outline-none focus:shadow-outline ">
                  Contactez nous
                </button>
              </div>
            </form>
          </div>
        </div>
      </>
  )
}

export default Login
