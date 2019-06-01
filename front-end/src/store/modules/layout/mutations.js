import Cookies from 'js-cookie'

const TOGGLE_SIDEBAR = (state) => {
  state.sidebar.opened = !state.sidebar.opened
  state.sidebar.withoutAnimation = false
  if (state.sidebar.opened) {
    Cookies.set('sidebarStatus', 1)
  } else {
    Cookies.set('sidebarStatus', 0)
  }
}

const CLOSE_SIDEBAR = (state, withoutAnimation) => {
  Cookies.set('sidebarStatus', 0)
  state.sidebar.opened = false
  state.sidebar.withoutAnimation = withoutAnimation
}

const TOGGLE_DEVICE = (state, device) => {
  state.device = device
}

const SET_LANGUAGE = (state, language) => {
  state.language = language
  Cookies.set('language', language)
}

const SET_SIZE = (state, size) => {
  state.size = size
  Cookies.set('size', size)
}

export default {
  TOGGLE_SIDEBAR,
  CLOSE_SIDEBAR,
  TOGGLE_DEVICE,
  SET_LANGUAGE,
  SET_SIZE
}
