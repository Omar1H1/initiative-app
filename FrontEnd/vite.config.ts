import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import tailwindcss from 'tailwindcss';
import autoprefixer from 'autoprefixer';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [
        tailwindcss,
        autoprefixer,
      ],
    },
  },
  define: {
    // Polyfill the global object with window for browser compatibility
    'global': 'window',
    // Polyfill process.env for environment variables
    'process.env': {},
  },
  resolve: {
    alias: {
      // Polyfill the `buffer` and `process` modules to be used in the browser
      buffer: path.resolve(__dirname, 'node_modules', 'buffer'),
      process: path.resolve(__dirname, 'node_modules', 'process'),
    },
  },
});
