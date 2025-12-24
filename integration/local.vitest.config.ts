import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    testTimeout: -1,
    globalSetup: 'env/local-global-setup.ts',
    fileParallelism: false,
    maxWorkers: 1,
    isolate: false,
  },
});
