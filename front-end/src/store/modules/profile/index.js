import actions from './actions'
import getters from './getters'
import mutations from './mutations'

const state = {
  profile: {
    avatarUrl: '',
    following: 0,
    followedBy: 0
  },
  viewProfile: {
    avatarUrl: '',
    following: 0,
    followedBy: 0
  }
}
export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
