import Vue from 'vue'
import i18n from '../lang' // Internationalization

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
