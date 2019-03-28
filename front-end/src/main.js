// Core of Vue
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import i18n from './lang/i18n'
// Libraries
import Vue2Filters from 'vue2-filters'
import BootstrapVue from 'bootstrap-vue'

// Style for application
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(Vue2Filters)
Vue.use(BootstrapVue)

// Config for Vue instance
Vue.config.productionTip = false

new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
