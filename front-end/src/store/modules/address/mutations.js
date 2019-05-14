const GET_ADDRESS_BY_USERID = (state, payload) => {
  state.addresses = Object.assign([], state.addresses, payload)
}

const GET_ADDRESS_BY_ID = (state, payload) => {
  state.selected = Object.assign({}, payload)
}

const CREATE_ADDRESS = (state, payload) => {
  console.log('created address', payload)
}
export default {
  GET_ADDRESS_BY_USERID,
  GET_ADDRESS_BY_ID,
  CREATE_ADDRESS
}
