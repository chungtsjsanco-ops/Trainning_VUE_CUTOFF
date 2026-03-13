import Vue from 'vue'
import Vuex from 'vuex'
import VuexPersistence from 'vuex-persist'
import { user } from './user'

Vue.use(Vuex)

const vuexPersist = new VuexPersistence({
  key: 'training-app',
  modules: ['user']
})

export const store = new Vuex.Store({
  modules: {
    user
  },
  plugins: [vuexPersist.plugin]
})
