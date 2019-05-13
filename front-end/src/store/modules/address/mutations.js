const GET_ADDRESS_BY_USERID = (state, payload) => {
  state.addresses = Object.assign([], state.addresses, payload)
}
const CREATE_ADDRESS = (state, payload) => {
  console.log('created address', payload)
}
export default {
  GET_ADDRESS_BY_USERID,
  CREATE_ADDRESS
}
