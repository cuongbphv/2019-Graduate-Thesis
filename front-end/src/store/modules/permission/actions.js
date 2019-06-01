import { allRoutes } from '@/router'
import { filterAsyncRoutes } from '@/utils/permission'
import role from '@/api/role'
import permission from '@/api/permission'
import { Status } from '@/utils/constants'
import CommonModelMap from '../../models/CommonModelMap'

const loadRoutesByAuthorities = ({ commit }, authorities) => {
  const accessedRoutes = filterAsyncRoutes(allRoutes, authorities)
  commit('LOAD_ROUTES_BY_AUTHORITIES', accessedRoutes)
}

const getListRoles = ({ commit }) => {
  return role.getListRole().then(res => {
    if (res.status === Status.SUCCESS) {
      commit('LOAD_LIST_ROLE', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const getListPermissions = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return permission.getListPermissions(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('LOAD_LIST_PERMISSION', res.data)
    }
  }).catch(error => {
    throw error
  })
}

export default {
  loadRoutesByAuthorities,
  getListRoles,
  getListPermissions
}
