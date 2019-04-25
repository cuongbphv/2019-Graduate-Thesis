import CommonModelMap from '../../models/CommonModelMap'

const REGISTER = (state, payload) => {
  const auth = new CommonModelMap(payload)
  state.auth = Object.assign({}, auth)
}

const LOGIN_LOCAL = (state, token) => {
  state.auth.token = token
}

const LOGIN_OAUTH2 = (state, token) => {
  state.auth.token = token
}

const CLEAR = (state) => {
  state.auth = {}
}

export default {
  REGISTER,
  LOGIN_LOCAL,
  LOGIN_OAUTH2,
  CLEAR
}
