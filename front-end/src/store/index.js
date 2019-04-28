import Vue from 'vue'
import Vuex from 'vuex'
import errorLog from './modules/errorLog'
import tagsView from './modules/tagsView'
import getters from './getters'
import auth from './modules/auth'
import layout from './modules/layout'
import profile from './modules/profile'
import permission from './modules/permission'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    auth,
    layout,
    profile,
    permission,
    errorLog,
    tagsView
  },
  getters
})
