import Vue from 'vue'
import Vuex from 'vuex'
import errorLog from './modules/errorLog'
import tagsView from './modules/tagsView'
import getters from './getters'
import auth from './modules/auth'
import layout from './modules/layout'
import profile from './modules/profile'
import permission from './modules/permission'
import location from './modules/location'
import category from './modules/category'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    auth,
    layout,
    profile,
    permission,
    location,
    category,
    errorLog,
    tagsView
  },
  getters
})
