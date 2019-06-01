const INIT_DATA = (state, payload) => {
  state.profile = Object.assign({}, state.profile, payload)
}

const CREATE_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload)
}

const UPDATE_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload)
}

const GET_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, state.profile, payload)
}

const UPDATE_SETTINGS = (state, payload) => {
  state.profile = Object.assign({}, state.profile, payload)
}

export default {
  INIT_DATA,
  CREATE_PROFILE,
  UPDATE_PROFILE,
  GET_PROFILE,
  UPDATE_SETTINGS
}
