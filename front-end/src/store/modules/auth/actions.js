import auth from '@/api/auth'
import { setToken, removeToken, removePermission } from '@/utils/auth'
import { Status } from '@/utils/constants'

const register = ({ commit }, params) => {
  return auth.register(params).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('REGISTER', res.data)
    }
    return res
  }).catch(error => {
    throw error
  })
}

const submitOTP = ({ commit }, params) => {
  return auth.submitOTP(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const sendOTP = ({ commit }, params) => {
  return auth.sendOTP(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const loginLocal = ({ commit }, user) => {
  return auth.loginLocal(user).then(res => {
    if (res.status === Status.SUCCESS) {
      setToken(res.data)
      commit('LOGIN_LOCAL', res.data)
    }
    return res
  }).catch(error => {
    throw error
  })
}

const loginOAuth2 = ({ commit }, token) => {
  setToken(token)
  commit('LOGIN_OAUTH2', token)
}

const clear = ({ commit }) => {
  removeToken()
  removePermission()
  commit('CLEAR')
}
export default {
  register,
  submitOTP,
  sendOTP,
  loginLocal,
  loginOAuth2,
  clear
}
