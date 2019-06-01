import Vue from 'vue'
import i18n from '../lang' // Internationalization
import { Status } from './constants'

export function showSuccess(key) {
  Vue.prototype.$message({
    message: i18n.t(key),
    type: 'success',
    duration: 3000
  })
}

export function showWarning(key) {
  Vue.prototype.$message({
    message: i18n.t(key),
    type: 'warning',
    duration: 3000
  })
}

export function showError(key) {
  Vue.prototype.$message({
    message: i18n.t(key),
    type: 'error',
    duration: 3000
  })
}

export function showMessageAfterCallAPI(res, key) {
  if (res.status === Status.SUCCESS) {
    Vue.prototype.$message({
      message: i18n.t(key),
      type: 'success',
      duration: 3000
    })
  } else {
    Vue.prototype.$message({
      message: i18n.t('code.' + res.status),
      type: 'error',
      duration: 3000
    })
    this.$message.error(this.$t())
  }
}
