import actions from './actions'
import getters from './getters'
import mutations from './mutations'
import Cookies from 'js-cookie'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  language: Cookies.get('language') || 'vi',
  size: Cookies.get('size') || 'medium'
}
export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
