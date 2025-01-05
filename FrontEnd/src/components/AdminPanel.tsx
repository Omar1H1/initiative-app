import Create from "../auth/Create.tsx";
import {useAtomValue} from "jotai";
import userAtom from "../service/UserAtom.tsx";
import NotFound from "./NotFound.tsx";

const AdminPanel = () => {
    const user = useAtomValue(userAtom);
    if(user?.role === "SUPERVISOR" || user?.role === "ADMIN") {
        return (
            <>
                <Create />
            </>
        )
    } else {
        return (
            <>
                <NotFound />
            </>
        )
    }

}

export default AdminPanel;