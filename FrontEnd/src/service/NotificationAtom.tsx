import { atom} from "jotai";
import {NotificationType} from "../types/Notification.ts";

const notificationAtom = atom<NotificationType[]>([]);
export default notificationAtom;