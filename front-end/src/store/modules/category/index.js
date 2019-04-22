import getters from './getters'
import actions from './actions'
import mutations from './mutations'

const state = {
  ids: [],
  entities: {},
  total: 0
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
