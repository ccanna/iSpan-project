import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    historyApiFallback: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 後端專案的網址與端口
        changeOrigin: true,            // 允許跨域
        // 如果後端 API 本身就有 /api 前綴，就不用 rewrite；
        // 如果後端 API 是直接 /config，則要用 rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
