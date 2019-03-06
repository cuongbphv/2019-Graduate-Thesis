module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    'plugin:vue/essential',
    "eslint:recommended",
    '@vue/standard'
  ],
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    "space-before-function-paren": 0,
    "no-shadow": [
      2,
      {
        "allow": [
          "state"
        ]
      }
    ]
  },
  parserOptions: {
    parser: 'babel-eslint'
  }
}
