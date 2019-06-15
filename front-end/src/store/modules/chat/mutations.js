const CREATE_CONVERSATION = (state, payload) => {
}

const GET_ALL_CONVERSATION = (state, payload) => {
}

const GET_CONVERSATION_BY_ID = (state, payload) => {
}

const ADD_MESSAGE = (state, payload) => {
}

const MESSAGE_HISTORY = (state, payload) => {
  function compare(a, b) { // sort messages by date
    if (a.createdDate > b.createdDate) {
      return 1
    }
    if (a.createdDate < b.createdDate) {
      return -1
    }
    return 0
  }
  state.messages = Object.assign([], payload.sort(compare))
}

export default {
  CREATE_CONVERSATION,
  GET_ALL_CONVERSATION,
  GET_CONVERSATION_BY_ID,
  ADD_MESSAGE,
  MESSAGE_HISTORY
}
