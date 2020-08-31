import Cookies from 'js-cookie'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  timeString: '',
  sysMap: null,
  infoWeathers: []
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_SYS_MAP: (state, map) => {
    state.sysMap = map;
  },
  SET_TIME_STRING: (state, time) => {
    state.timeString = time;
  },
  SET_INFO_WEATHER: (state, infoWeathers) => {
    state.infoWeathers = infoWeathers;
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setSysMap({ commit }, map) {
    commit('SET_SYS_MAP', map);
  },
  setTimeString({ commit }, time) {
    commit('SET_TIME_STRING', time);
  },
  setInfoWeathers({ commit }, infoWeathers) {
    commit('SET_INFO_WEATHER', infoWeathers);
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
