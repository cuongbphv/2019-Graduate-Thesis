const CREATE_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload.data)
}

const UPDATE_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload.data)
}

const GET_PROFILE = (state, payload) => {
  state.profile = Object.assign({}, payload.data)
}

export default {
  CREATE_PROFILE,
  UPDATE_PROFILE,
  GET_PROFILE
}
