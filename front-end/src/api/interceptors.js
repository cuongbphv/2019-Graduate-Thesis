import store from '../store'
import camelcaseKeys from 'camelcase-keys'
// import router from '../router'

function setup(instance) {
  instance.interceptors.request.use(
    function(config) {
      const token = store.getters['auth/token']
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

function showSpinner (instance) {
  instance.interceptors.request.use(
    request => {
      store.dispatch('Layout/showSpinner')
      return request
    },
    error => {
      store.dispatch('Layout/showSpinner')
      return Promise.reject(error)
    }
  )
}

function hideSpinner(instance) {
  instance.interceptors.response.use(
    response => {
      setTimeout(() => {
        store.dispatch('Layout/hideSpinner')
      }, 500)
      return response
    },
    error => {
      setTimeout(() => {
        store.dispatch('Layout/hideSpinner')
      }, 500)
      return Promise.reject(error)
    }
  )
}

function checkError(instance) {
  instance.interceptors.response.use(
    response => {
      if (response.data.status === 'error') {
        const { data: { errorMessage: { errorCode } = {} } = {} } = response.data
        if (errorCode && [22, 71, 73].includes(errorCode) &&
          !window.location.pathname.includes('login')
        ) {
          // router.push({name: 'ErrorPage', params: { errorCode }})
          // window.location.href = window.location.origin + '/error/' + errorCode
        }

        return Promise.reject(response.data.data.errorMessage)
      }

      return response
    },
    error => {
      if (
        [403, 404, 500, 503].includes(error.response.status) &&
        !window.location.pathname.includes('login')
      ) {
        // router.push({name: 'ErrorPage', params: { errorCode: error.response.status }})
        // window.location.href = window.location.origin + '/error/' + error.response.status
      }
      return Promise.reject(error)
    }
  )
}

export default {
  setup,
  checkToken,
  hideSpinner,
  showSpinner,
  checkError
}
