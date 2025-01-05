export default {
  content: [
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        accent: {
          1: "hsl(var(--color-accent-1) / 1)",
          2: "hsl(var(--color-accent-2) / 1)",
        },
        bkg: "hsl(var(--color-bkg) / 1)",
        content: "hsl(var(--color-content) / 1)",
      },
      animation: {
        "spin-slower": "spin 35s ease infinite",
        "spin-slow": "spin 25s ease-in-out infinite reverse",
      },
    },
  },
  plugins: [],
};
