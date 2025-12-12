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
// Note: To run only one test file at a time (no file-level parallelism), use the Vitest CLI with the --runInBand flag if supported, e.g., `vitest --runInBand`.
