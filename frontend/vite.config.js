import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),tailwindcss(),
  ],
  optimizeDeps: {
    include: [
      '@react-pdf/renderer',
      '@react-pdf/font',
      '@react-pdf/layout',
      '@react-pdf/pdfkit',
      '@react-pdf/image',
      '@react-pdf/textkit'
    ],
    esbuildOptions: {
      target: 'es2020'
    },
    exclude: ["@emailjs/browser"],
  },
  build: {
    target: 'es2020'
  }
  
})
