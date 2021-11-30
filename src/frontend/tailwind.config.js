module.exports = {
  mode: "jit",
  purge: ["./pages/**/*.{js,ts,jsx,tsx}", "./components/**/*.{js,ts,jsx,tsx}"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      boxShadow: {
        '3xl': "0 0 10px rgba(0,0,0, 0.4)"
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
};
