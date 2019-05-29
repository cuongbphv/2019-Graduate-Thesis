import Permission from '../../models/Permission'

const LOAD_ROUTES_BY_AUTHORITIES = (state, payload) => {
  state.routes = payload
}

const LOAD_LIST_ROLE = (state, payload) => {
  state.roles = [...payload]
}

const LOAD_LIST_PERMISSION = (state, payload) => {
  const permissions = payload.map(permission => new Permission(permission))
  state.permissions = [...permissions]
}

export default {
  LOAD_ROUTES_BY_AUTHORITIES,
  LOAD_LIST_ROLE,
  LOAD_LIST_PERMISSION
}
