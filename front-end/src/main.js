// Core of Vue
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import i18n from './lang/i18n'
// Libraries
import Vue2Filters from 'vue2-filters'
// Style for application
import './styles/style.scss'

Vue.use(Vue2Filters)

// Config for Vue instance
Vue.config.productionTip = false
new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
