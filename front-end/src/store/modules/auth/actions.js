import auth from '../../../api/auth'
import { Status } from '../../../utils/constants'
const register = ({ commit }, params) => {
  return auth.register(params).then(res => {
    if (res.status === Status.CREATED) {
      commit('REGISTER', params)
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const loginLocal = ({ commit }, user) => {
  return auth.loginLocal(user).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('LOGIN_LOCAL', res.data)
    }
  }).catch(error => {
    throw error
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
  return auth.logout().then(() => {
    localStorage.clear()
    sessionStorage.clear()
    commit('CLEAR')
  }).catch(error => {
    throw error
  })
}
export default {
  register,
  loginLocal,
  clear
}
