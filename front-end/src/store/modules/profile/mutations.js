const INIT_DATA = (state, payload) => {
  state.profile = Object.assign({}, state.profile, payload)
}

const CREATE_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload)
  state.viewProfile = Object.assign({}, payload)
}

const UPDATE_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload)
  state.viewProfile = Object.assign({}, payload)
}

const GET_PROFILE = (state, payload) => {
  state.viewProfile = Object.assign({}, payload)
}

const UPDATE_SETTINGS = (state, payload) => {
  state.profile = Object.assign({}, state.profile, payload)
  state.viewProfile = Object.assign({}, payload)
}

const FOLLOW_USER = (state, payload) => {
}

const UNFOLLOW_USER = (state, payload) => {
}

export default {
  INIT_DATA,
  CREATE_PROFILE,
  UPDATE_PROFILE,
  GET_PROFILE,
  UPDATE_SETTINGS,
  FOLLOW_USER,
  UNFOLLOW_USER
}
