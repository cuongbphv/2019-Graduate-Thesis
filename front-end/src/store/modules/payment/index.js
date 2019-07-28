import actions from './actions'
import getters from './getters'
import mutations from './mutations'

const state = {
  account: 0,
  rate: 0,
  history: []
}
export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
