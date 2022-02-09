import { InitialOptionsTsJest } from 'ts-jest'

const config: InitialOptionsTsJest = {
  preset: 'ts-jest',
  testEnvironment: require.resolve('./env/default/test-environment.ts'),
}

export default config
