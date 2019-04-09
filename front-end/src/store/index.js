import Vue from 'vue'
import Vuex from 'vuex'
import errorLog from './modules/errorLog'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import getters from './getters'
import auth from './modules/auth'
import layout from './modules/layout'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    auth,
    layout,
    errorLog,
    permission,
    tagsView,
    user
  },
  getters
})
