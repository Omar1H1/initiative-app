import Hero from "./Hero.tsx";
import Profiles from "./Profiles.tsx";
import { useAtomValue } from "jotai";
import loginAtom from "../service/LoginState.tsx";

const Home = () => {
  const token = useAtomValue(loginAtom);

  return (<>
    {token ? <Profiles /> : <Hero />}
  </>);
};

export default Home;
