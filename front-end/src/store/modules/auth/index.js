import actions from './actions'
import getters from './getters'
import mutations from './mutations'

const state = {
  auth: {
    token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1Y2FlNGM4ODZiZjQ2ZDBiNTQ1M2E2NmYiLCJpYXQiOjE1NTU0MzMyNjcsImV4cCI6MTU1NjI5NzI2N30.1uqIr0RTjmjJTAUkwoKzXwhSZdyVWDwSgRT_mqyhWO92zWJly5zi53QiLYNBlBGp3i6eGSPRD2YRrSC5kDhwvQ'
  }
}
export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
