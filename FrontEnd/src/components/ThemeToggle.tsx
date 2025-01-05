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

        // Update the HTML class
        if (newTheme === "dark") {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }

        // Persist to localStorage
        localStorage.setItem("theme", newTheme);

        // Debugging logs
        console.log("Theme toggled to:", newTheme);
        console.log("HTML classList after toggle:", document.documentElement.classList);
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

        // Debugging logs
        console.log("Initial theme set to:", storedTheme);
    }, [setTheme]);

    return (
        <button onClick={handleToggle} className="p-2">
            {theme === "dark" ? <CiLight className="text-4xl duration-500 ease-in-out on hover:bg-yellow-200 rounded-lg"/> : <MdDarkMode className="text-4xl transition duration-500 ease-in-out on hover:bg-black rounded-lg"/>}
        </button>
    );
};

export default ThemeToggle;
