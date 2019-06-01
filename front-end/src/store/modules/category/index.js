import getters from './getters'
import actions from './actions'
import mutations from './mutations'

const state = {
  ids: [],
  entities: {},
  metadata: [],
  totalElements: 0,
  totalPages: 0
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
