import Vue from 'vue'
import App from './App'
import router from './router'
import { store } from './store'

// Bootstrap Vue
import bootstrapVuePlugin from './plugins/bootstrapVuePlugin'
// Toast notification
import toastPlugin from './plugins/toastPlugin'
// Axios
import VueAxios from 'vue-axios'
import axiosPlugin from './plugins/axiosPlugin'
// Auth plugin
import Auth from './plugins/auth'

// Filters
import { currency } from './filters/currency'
import { numeral } from './filters/numeral'

import requestContext from './utils/RequestContext'

Vue.config.productionTip = false

Vue.use(Auth)
Vue.use(toastPlugin)
Vue.use(VueAxios, axiosPlugin)
Vue.use(bootstrapVuePlugin)

Vue.filter('currency', currency)
Vue.filter('numeral', numeral)

Vue.prototype.$requestContext = requestContext

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
  data: {
    context: {
      get: async function(url, params) {
        return await requestContext.get(url, params)
      },
      post: async function(url, data) {
        return await requestContext.post(url, data)
      },
      getWithOutToken: async function(url, params) {
        return await requestContext.getWithOutToken(url, params)
      },
      postWithOutToken: async function(url, data) {
        return await requestContext.postWithOutToken(url, data)
      },
      isAuthenticated: function() {
        return Vue.auth.isAuthenticated()
      },
      authenticate: function(tokenData) {
        Vue.auth.authenticate(tokenData)
      },
      logOut: function() {
        Vue.auth.destroy()
      },
      user: {
        getUserName: function() {
          return Vue.auth.getUserName()
        },
        getRoles: function() {
          return Vue.auth.getRoles()
        }
      }
    }
  },
  methods: {
    showLoading: function(value) {
      if (value) {
        document.getElementById('app-loading') && (document.getElementById('app-loading').style.display = 'flex')
      } else {
        document.getElementById('app-loading') && (document.getElementById('app-loading').style.display = 'none')
      }
    },
    toastSuccess: function(arg) {
      this.$toast.success(arg)
    },
    toastError: function(arg) {
      this.$toast.error(arg)
    },
    toastWarning: function(arg) {
      this.$toast.warning(arg)
    },
    toastInfo: function(arg) {
      this.$toast.info(arg)
    }
  }
})
