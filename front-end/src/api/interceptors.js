import camelcaseKeys from 'camelcase-keys'
import store from '../store'
import { getToken } from '../utils/auth'

function setup(instance) {
  instance.interceptors.request.use(
    function(config) {
      const token = getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    },
    function(err) {
      return Promise.reject(err)
    }
  )
}

function checkToken(instance) {
  instance.interceptors.response.use(
    response => {
      if (response.headers['content-type'].startsWith('application/json')) {
        response.data = camelcaseKeys(response.data, { deep: true })
      }
      return response
    },
    error => {
      if (
        error.response.status === 401 &&
        !window.location.pathname.includes('login')
      ) {
        store.dispatch('auth/clear')
        // router.push({name: 'login'})
        window.location.href = window.location.origin + '/login'
      }
      return Promise.reject(error)
    }
  )
}

export default {
  setup,
  checkToken
}
