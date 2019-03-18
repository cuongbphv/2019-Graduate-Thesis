const user = state => { return state.auth.user || getUser() }

const token = state => state.auth.token || sessionStorage.getItem('token') || localStorage.getItem('token')

const getUser = () => {
  return JSON.parse(sessionStorage.getItem('user')) ||
    JSON.parse(localStorage.getItem('user')) || {
    email: '',
    username: ''
  }
}

export default {
  user,
  token
}
