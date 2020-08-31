import Cookies from 'js-cookie'

const TokenKey = 'token'
const userKey = 'user'


export function setToken(token) {
  return Cookies.set(TokenKey, token)
}
export function getToken() {
  return Cookies.get(TokenKey)
}
export function removeToken() {
  return Cookies.remove(TokenKey)
}

//
export function setUserId(id) {
  return Cookies.set(userKey, id)
}

export function getUserId() {
  return Cookies.get(userKey)
}

export function removeUserId() {
  return Cookies.remove(userKey)
}
