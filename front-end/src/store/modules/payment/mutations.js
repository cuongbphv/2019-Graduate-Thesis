const GET_ACCOUNT = (state, payload) => {
  state.account = payload
}

const CURRENCY_RATE = (state, payload) => {
  state.rate = payload
}

const GET_PAYMENT_HISTORY = (state, payload) => {
  state.history = Object.assign([], payload)
}

export default {
  GET_ACCOUNT,
  CURRENCY_RATE,
  GET_PAYMENT_HISTORY
}
