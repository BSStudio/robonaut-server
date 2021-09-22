/** @type {import('ts-jest/dist/types').InitialOptionsTsJest} */
module.exports = {
  reporters: ['default', ['jest-junit', { outputDirectory: 'out' }]],
  preset: 'ts-jest',
  testEnvironment: './env/testEnvironment.ts',
  globalSetup: './env/globalSetup.ts',
  globalTeardown: './env/globalTeardown.ts',
}
