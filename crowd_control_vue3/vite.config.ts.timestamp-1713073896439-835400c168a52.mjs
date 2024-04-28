// vite.config.ts
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "file:///C:/Users/%E5%8D%B7%E6%9F%8F/Desktop/crowd_control_vue3/node_modules/vite/dist/node/index.js";
import vue from "file:///C:/Users/%E5%8D%B7%E6%9F%8F/Desktop/crowd_control_vue3/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import { viteMockServe } from "file:///C:/Users/%E5%8D%B7%E6%9F%8F/Desktop/crowd_control_vue3/node_modules/vite-plugin-mock/dist/index.js";
var __vite_injected_original_import_meta_url = "file:///C:/Users/%E5%8D%B7%E6%9F%8F/Desktop/crowd_control_vue3/vite.config.ts";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    viteMockServe({
      supportTs: true,
      mockPath: "./src/mock"
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxcdTUzNzdcdTY3Q0ZcXFxcRGVza3RvcFxcXFxjcm93ZF9jb250cm9sX3Z1ZTNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXFx1NTM3N1x1NjdDRlxcXFxEZXNrdG9wXFxcXGNyb3dkX2NvbnRyb2xfdnVlM1xcXFx2aXRlLmNvbmZpZy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvJUU1JThEJUI3JUU2JTlGJThGL0Rlc2t0b3AvY3Jvd2RfY29udHJvbF92dWUzL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHsgZmlsZVVSTFRvUGF0aCwgVVJMIH0gZnJvbSAnbm9kZTp1cmwnXG5cbmltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGUnXG5pbXBvcnQgdnVlIGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZSdcbmltcG9ydCB7IHZpdGVNb2NrU2VydmUgfSBmcm9tICd2aXRlLXBsdWdpbi1tb2NrJ1xuLy8gaHR0cHM6Ly92aXRlanMuZGV2L2NvbmZpZy9cbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XG4gICAgcGx1Z2luczogW1xuICAgICAgICB2dWUoKSxcbiAgICAgICAgdml0ZU1vY2tTZXJ2ZSh7XG4gICAgICAgICAgICBzdXBwb3J0VHM6IHRydWUsXG4gICAgICAgICAgICBtb2NrUGF0aDogJy4vc3JjL21vY2snLFxuICAgICAgICB9KVxuICAgIF0sXG4gICAgcmVzb2x2ZToge1xuICAgICAgICBhbGlhczoge1xuICAgICAgICAgICAgJ0AnOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vc3JjJywgaW1wb3J0Lm1ldGEudXJsKSlcbiAgICAgICAgfVxuICAgIH1cbn0pIl0sCiAgIm1hcHBpbmdzIjogIjtBQUE4VCxTQUFTLGVBQWUsV0FBVztBQUVqVyxTQUFTLG9CQUFvQjtBQUM3QixPQUFPLFNBQVM7QUFDaEIsU0FBUyxxQkFBcUI7QUFKK0osSUFBTSwyQ0FBMkM7QUFNOU8sSUFBTyxzQkFBUSxhQUFhO0FBQUEsRUFDeEIsU0FBUztBQUFBLElBQ0wsSUFBSTtBQUFBLElBQ0osY0FBYztBQUFBLE1BQ1YsV0FBVztBQUFBLE1BQ1gsVUFBVTtBQUFBLElBQ2QsQ0FBQztBQUFBLEVBQ0w7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNMLE9BQU87QUFBQSxNQUNILEtBQUssY0FBYyxJQUFJLElBQUksU0FBUyx3Q0FBZSxDQUFDO0FBQUEsSUFDeEQ7QUFBQSxFQUNKO0FBQ0osQ0FBQzsiLAogICJuYW1lcyI6IFtdCn0K
