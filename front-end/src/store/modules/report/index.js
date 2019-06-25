import actions from './actions'
import getters from './getters'
import mutations from './mutations'

const state = {
  ids: [],
  entities: {},
  totalElements: 0,
  totalPages: 0
}
export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
