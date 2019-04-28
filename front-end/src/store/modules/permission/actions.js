import { allRoutes } from '@/router'
import { filterAsyncRoutes } from '@/utils/permission'

const loadRoutesByAuthorities = ({ commit }, authorities) => {
  const accessedRoutes = filterAsyncRoutes(allRoutes, authorities)
  commit('LOAD_ROUTES_BY_AUTHORITIES', accessedRoutes)
}

export default {
  loadRoutesByAuthorities
}
