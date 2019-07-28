const listUser = state => {
  return state.ids.map(id => state.entities[id])
}

const listUserExceptAdmin = state => {
  return state.ids.map(id => state.entities[id])
}

const totalElements = state => state.totalElements

export default {
  listUser,
  totalElements,
  listUserExceptAdmin
}
