import Vue from 'vue'
import Cookies from 'js-cookie'
import Element from 'element-ui'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faFacebook, faGoogle, faLinkedin, faGithub } from '@fortawesome/free-brands-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import 'element-ui/lib/theme-chalk/reset.css'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // Internationalization
import './icons' // icon
import './errorLog' // error log
import './permission' // permission control
import * as filters from './filters' // global filters
import InfiniteLoading from 'vue-infinite-loading'

library.add(faFacebook, faGoogle, faLinkedin, faGithub)

Vue.component('font-awesome-icon', FontAwesomeIcon)

// use element-ui and some options
Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Vue.use(InfiniteLoading, {
  props: {
    spinner: 'default'
    /* other props need to configure */
  },
  system: {
    throttleLimit: 500
    /* other settings need to configure */
  },
  slots: {
    // keep default styles
    noResults: '',

    // remove default styles
    noMore: ''
  }
})

// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
