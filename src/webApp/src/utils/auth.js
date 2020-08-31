import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'
const userKey = 'remember_user';


export function rememberUser (user) {
  return Cookies.set(userKey, user)
}
export function getUser () {
  return Cookies.get(userKey)
}
export function deleteUser () {
  return Cookies.remove(userKey)
}

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
