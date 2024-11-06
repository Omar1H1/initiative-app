import {Context, createContext} from "react";

// @ts-ignore
const isLogged: Context<[string | null, React.Dispatch<React.SetStateAction<string | null>>]> = createContext([]);

export default isLogged;
