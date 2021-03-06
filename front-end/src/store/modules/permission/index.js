import getters from './getters'
import actions from './actions'
import mutations from './mutations'

const state = {
  routes: [],
  roles: [],
  permissions: []
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
