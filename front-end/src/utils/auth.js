import Cookies from 'js-cookie'

const TokenKey = 'TOKEN'
const Permission = 'PERMISSION'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getPermission() {
  return Cookies.get(Permission)
}

export function setPermission(permissions) {
  return Cookies.set(Permission, permissions)
}

export function removePermission() {
  return Cookies.remove(Permission)
}
