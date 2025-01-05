import { atom} from "jotai";
import {User} from "../types/User.ts";

const userAtom = atom<User | null>(null);

export default userAtom;