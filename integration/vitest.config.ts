import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    testTimeout: -1,
    globalSetup: 'env/global-setup.ts',
    fileParallelism: false,
  },
});
