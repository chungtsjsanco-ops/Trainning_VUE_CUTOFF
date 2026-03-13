export const user = {
  namespaced: true,
  state: {
    username: null,
    roles: []
  },
  getters: {
    username: state => state.username,
    roles: state => state.roles,
    isLoggedIn: state => !!state.username
  },
  mutations: {
    setUser(state, payload) {
      state.username = payload.username
      state.roles = payload.roles || []
    },
    clearUser(state) {
      state.username = null
      state.roles = []
    }
  },
  actions: {
    login({ commit }, userData) {
      commit('setUser', userData)
    },
    logout({ commit }) {
      commit('clearUser')
    }
  }
}
