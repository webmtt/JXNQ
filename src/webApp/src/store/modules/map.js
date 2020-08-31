const state = {
  showLayers: [],
  businessLayers: [],
  lengendLayers: [],
  playLayers: [],
  playerYears: [],
}

const mutations = {
  CHANGE_ACTIVE_LAYERS: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  CHANGE_BUSINESS_LAYERS: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  CHANGE_LENGEND_LAYERS: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  CHANGE_PLAYER_LAYERS: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  CHANGE_PLAYER_YEARS: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeActiveLayers({ commit }, data) {
    commit('CHANGE_ACTIVE_LAYERS', data)
  },
  changeBusinessLayers({ commit }, data) {
    commit('CHANGE_BUSINESS_LAYERS', data)
  },
  changeLengendLayers({ commit }, data) {
    commit('CHANGE_LENGEND_LAYERS', data)
  },
  changePlayLayers({ commit }, data) {
    commit('CHANGE_PLAYER_LAYERS', data)
  },
  changePlayYears({ commit }, data) {
    commit('CHANGE_PLAYER_YEARS', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

