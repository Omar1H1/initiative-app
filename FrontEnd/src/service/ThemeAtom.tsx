import { atom} from "jotai";
import {Themes} from "../types/Themes.ts";

const ThemeAtom = atom< Themes | null>(null);

export default ThemeAtom;