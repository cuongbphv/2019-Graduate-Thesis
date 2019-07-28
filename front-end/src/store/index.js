import Vue from 'vue'
import Vuex from 'vuex'
import errorLog from './modules/errorLog'
import tagsView from './modules/tagsView'
import getters from './getters'
import auth from './modules/auth'
import advertising from './modules/advertising'
import category from './modules/category'
import chat from './modules/chat'
import notification from './modules/notification'
import rating from './modules/rating'
import layout from './modules/layout'
import location from './modules/location'
import permission from './modules/permission'
import profile from './modules/profile'
import address from './modules/address'
import report from './modules/report'
import sysAdminUser from './modules/sys-admin-user'
import user from './modules/user'
import payment from './modules/payment'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    address,
    auth,
    advertising,
    category,
    chat,
    notification,
    rating,
    layout,
    location,
    permission,
    profile,
    report,
    sysAdminUser,
    user,
    payment,
    errorLog,
    tagsView
  },
  getters
})
