import Vue from 'vue'
import Vuex from 'vuex'
import errorLog from './modules/errorLog'
import tagsView from './modules/tagsView'
import getters from './getters'
import auth from './modules/auth'
import advertising from './modules/advertising'
import category from './modules/category'
import chat from './modules/chat'
import rating from './modules/rating'
import layout from './modules/layout'
import location from './modules/location'
import permission from './modules/permission'
import profile from './modules/profile'
import address from './modules/address'
import sysAdminUser from './modules/sys-admin-user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    address,
    auth,
    advertising,
    category,
    chat,
    rating,
    layout,
    location,
    permission,
    profile,
    sysAdminUser,
    errorLog,
    tagsView
  },
  getters
})
