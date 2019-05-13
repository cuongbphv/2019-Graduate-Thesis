import Vue from 'vue'
import Vuex from 'vuex'
import errorLog from './modules/errorLog'
import tagsView from './modules/tagsView'
import getters from './getters'
import auth from './modules/auth'
import advertising from './modules/advertising'
import category from './modules/category'
import layout from './modules/layout'
import location from './modules/location'
import permission from './modules/permission'
import profile from './modules/profile'
import address from './modules/address'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    address,
    auth,
    advertising,
    category,
    layout,
    location,
    permission,
    profile,
    errorLog,
    tagsView
  },
  getters
})
