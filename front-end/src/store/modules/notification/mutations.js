const GET_NOTIFICATIONS = (state, payload) => {
  state.notifications = Object.assign([], payload)
}

export default {
  GET_NOTIFICATIONS
}
