const GET_ADDRESS_BY_USERID = (state, payload) => {
  state.addresses = Object.assign([], payload)
}

const GET_ADDRESS_BY_ID = (state, payload) => {
  state.selected = Object.assign({}, payload)
}

const CREATE_ADDRESS = (state, payload) => {
  console.log('created address', payload)
}
const UPDATE_ADDRESS = (state, payload) => {
  console.log('updated address', payload)
}
export default {
  GET_ADDRESS_BY_USERID,
  GET_ADDRESS_BY_ID,
  CREATE_ADDRESS,
  UPDATE_ADDRESS
}
