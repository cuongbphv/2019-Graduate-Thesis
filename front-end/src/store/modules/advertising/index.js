import getters from './getters'
import actions from './actions'
import mutations from './mutations'

const state = {
  entities: {},
  ids: [],
  totalPages: 0,
  totalElements: 0,
  classifiedAds: {},
  topNewClassifiedAds: {
    content: [],
    totalPages: 0,
    totalElements: 0
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
