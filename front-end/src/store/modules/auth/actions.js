import auth from '@/api/auth'
import { setToken, removeToken, removePermission } from '@/utils/auth'
import { Status } from '@/utils/constants'

const register = ({ commit }, params) => {
  return auth.register(params).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('REGISTER', res.data)
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
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
  loginLocal,
  loginOAuth2,
  clear
}
