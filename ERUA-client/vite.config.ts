import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
  },
  resolve: {
    alias: [
      {
        find: '~',
        replacement: resolve(__dirname, './src'),
      },
    ],
  },
  build: {
    target: 'esnext',
  },
});
