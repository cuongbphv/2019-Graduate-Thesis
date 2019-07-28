const CREATE_RATING = (state, payload) => {
}

const GET_RATING = (state, payload) => {
}

const GET_ALL_RATING = (state, payload) => {
}

const UPDATE_RATING = (state, payload) => {
}

const DELETE_RATING = (state, payload) => {
}

const GET_RATING_FOR_USER = (state, payload) => {
  state.ratings = Object.assign([], payload)
}

export default {
  CREATE_RATING,
  GET_RATING,
  GET_ALL_RATING,
  UPDATE_RATING,
  DELETE_RATING,
  GET_RATING_FOR_USER
}
