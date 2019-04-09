const REGISTER = (state, payload) => {
  const user = {
    email: payload.email
  }
  state.user = Object.assign({}, user)
}

const LOGIN_LOCAL = (state, payload) => {
  state.auth = Object.assign({}, payload)
}

const LOG_OUT = (state) => {
  state.auth = {
    token: '',
    user: {}
  }
}

export default {
  REGISTER,
  LOGIN_LOCAL,
  LOG_OUT
}
