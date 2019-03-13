import auth from '../../../api/auth'
const signin = ({ commit }, user) => {
  return auth.signin(user).then((res) => {
    if (user.remember) {
      localStorage.setItem('token', res.data.token)
    } else {
      sessionStorage.setItem('token', res.data.token)
    }
    localStorage.setItem('user', JSON.stringify(res.data.user))
    commit('SIGN_IN', res.data)
    return res.data
  }).catch(error => {
    throw error
  })
}
const clear = ({ commit }) => {
  localStorage.clear()
  sessionStorage.clear()
  commit('CLEAR')
}
export default {
  signin,
  clear
}
