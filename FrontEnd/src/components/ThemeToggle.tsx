import { useEffect } from "react";
import { useAtom } from "jotai";
import themeAtom from "../service/ThemeAtom";
import { MdDarkMode } from "react-icons/md";
import { CiLight } from "react-icons/ci";


const ThemeToggle = () => {
    const [theme, setTheme] = useAtom(themeAtom);

    const handleToggle = () => {
        const newTheme = theme === "light" ? "dark" : "light";
        // @ts-ignore
        setTheme(newTheme);

        if (newTheme === "dark") {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }

        localStorage.setItem("theme", newTheme);
    };

    useEffect(() => {
        const storedTheme = localStorage.getItem("theme") || "light";
        // @ts-ignore
        setTheme(storedTheme);

        if (storedTheme === "dark") {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
    }, [setTheme]);

    return (
        <button onClick={handleToggle} className="p-2">
            {theme === "dark" ? (
                <CiLight
                    className="text-4xl duration-500 ease-in-out hover:bg-yellow-500 rounded-lg"
                />
            ) : (
                <MdDarkMode
                    className="text-4xl transition duration-500 ease-in-out hover:bg-black rounded-lg"
                />
            )}
        </button>
    );
};

export default ThemeToggle;
