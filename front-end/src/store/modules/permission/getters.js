const routes = state => state.routes

const roles = state => state.roles

const permissions = state => state.permissions

const permissionByRoleId = state => id => state.roles.find(role => role.id === id).permissions

export default {
  routes,
  roles,
  permissions,
  permissionByRoleId
}
