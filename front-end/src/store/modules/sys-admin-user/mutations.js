import CommonModelMap from '../../models/CommonModelMap'

const SYS_ADMIN_GET_LIST_USER = (state, payload) => {
  if (payload.content.length > 0) {
    const newUserIds = Array.from(new Set(payload.content.map(user => user.userId)))
    const newUserEntities = payload.content.reduce((users, user) => {
      users[user.userId] = new CommonModelMap(user)
      return users
    }, {})
    state.totalElements = payload.totalElements
    state.ids = newUserIds
    state.entities = Object.assign({}, state.entities, newUserEntities)
  } else {
    state.totalElements = 0
    state.ids = Object.assign([], [])
    state.entities = Object.assign({}, {})
  }
}

export default {
  SYS_ADMIN_GET_LIST_USER
}
