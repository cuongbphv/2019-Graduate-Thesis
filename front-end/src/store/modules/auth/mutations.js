const SIGN_IN = (state, payload) => {
  state.auth = Object.assign({}, payload)
}

const CLEAR = (state) => {
  state.auth = {
    token: '',
    user: {}
  }
}

export default {
  SIGN_IN,
  CLEAR
}
