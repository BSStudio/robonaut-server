module.exports = {
  parser: '@typescript-eslint/parser',
  env: {
    node: true,
    jest: true,
  },
  parserOptions: {
    sourceType: 'module',
  },
  extends: ['plugin:prettier/recommended', 'plugin:jest/all', 'plugin:@typescript-eslint/recommended', 'prettier'],
  plugins: ['jest', 'prettier'],
  rules: {
    'jest/no-hooks': 'off',
  },
}
