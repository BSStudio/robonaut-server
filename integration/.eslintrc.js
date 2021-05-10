module.exports = {
  env: {
    es2021: true,
    jest: true,
    node: true,
  },
  parserOptions: {
    sourceType: 'module',
  },
  extends: ['prettier', 'plugin:prettier/recommended', 'plugin:jest/all'],
  plugins: ['prettier', 'jest'],
  rules: {
    'jest/no-hooks': 'off',
    'sort-imports': 'error',
  },
}
