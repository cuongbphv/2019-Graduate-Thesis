import actions from './actions'
import getters from './getters'
import mutations from './mutations'

const state = {
  auth: {
    token: '',
    user: undefined
  }
}
export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
