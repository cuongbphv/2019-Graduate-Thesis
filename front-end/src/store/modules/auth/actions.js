import auth from '../../../api/auth'
import { setToken, removeToken } from '../../../utils/auth'
import { Status } from '../../../utils/constants'

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

const initData = ({ commit }) => {
  return auth.initData().then(res => {
    if (res.status === Status.SUCCESS) {
      commit('INIT_DATA', res.data)
    }
  })
}

// const loginOAuth2 = ({ commit }, type) => {
//   return auth.loginOAuth2(type).then((res) => {
//     console.log(res)
//   }).catch(error => {
//     throw error
//   })
// }

const clear = ({ commit }) => {
  removeToken()
  commit('CLEAR')
}
export default {
  register,
  loginLocal,
  initData,
  clear
}
