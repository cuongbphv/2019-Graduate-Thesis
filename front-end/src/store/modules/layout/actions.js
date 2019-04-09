const toggleSideBar = ({ commit }) => {
  commit('TOGGLE_SIDEBAR')
}

const closeSideBar = ({ commit }, { withoutAnimation }) => {
  commit('CLOSE_SIDEBAR', withoutAnimation)
}

const toggleDevice = ({ commit }, device) => {
  commit('TOGGLE_DEVICE', device)
}

const setLanguage = ({ commit }, language) => {
  commit('SET_LANGUAGE', language)
}

const setSize = ({ commit }, size) => {
  commit('SET_SIZE', size)
}

export default {
  toggleSideBar,
  closeSideBar,
  toggleDevice,
  setLanguage,
  setSize
}
