const GET_ADDRESS_BY_USERID = (state, payload) => {
  state.addresses = Object.assign([], payload)
}
const CREATE_ADDRESS = (state, payload) => {
  console.log('created address', payload)
}
const UPDATE_ADDRESS = (state, payload) => {
  console.log('updated address', payload)
}
export default {
  GET_ADDRESS_BY_USERID,
  CREATE_ADDRESS,
  UPDATE_ADDRESS
}
