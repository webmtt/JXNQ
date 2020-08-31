import { loginIn, getInfo, logout } from '@/api/user'
import { getToken, setToken, removeToken, setUserId, getUserId, removeUserId } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    info: null,
    userId: getUserId()
  }
}

const state = getDefaultState()

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_ID: (state, id) => {
    state.userId = id
  },
  SET_INFO: (state, info) => {
    state.info = info
  },
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      loginIn({ userName: username.trim(), password }).then(response => {
        const { token, userId } = response.data
        commit('SET_TOKEN', token);
        commit('SET_USER_ID', userId);
        setToken(token)
        setUserId(userId)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit }, userId) {
    return new Promise((resolve, reject) => {
      getInfo({userId}).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }
        // cookie记录用户信息
        commit('SET_INFO', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        removeToken() // must remove  token  first
        removeUserId()
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken();
      removeUserId();
      commit('RESET_STATE');
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

