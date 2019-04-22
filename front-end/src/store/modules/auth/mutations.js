import CommonModelMap from '../../models/CommonModelMap'

const REGISTER = (state, payload) => {
  const auth = new CommonModelMap(payload)
  state.auth = Object.assign({}, auth)
}

const LOGIN_LOCAL = (state, token) => {
  state.auth.token = token
}

const INIT_DATA = (state, data) => {
  state.auth = Object.assign({}, state.auth, data)
}

const CLEAR = (state) => {
  state.auth = {}
}

export default {
  REGISTER,
  LOGIN_LOCAL,
  INIT_DATA,
  CLEAR
}
