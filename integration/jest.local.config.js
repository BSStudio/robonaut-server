/** @type {import('ts-jest/dist/types').InitialOptionsTsJest} */
module.exports = {
  reporters: ['default', ['jest-junit', { outputDirectory: 'out' }]],
  preset: 'ts-jest',
  testEnvironment: './env/local.testEnvironment.ts',
}
