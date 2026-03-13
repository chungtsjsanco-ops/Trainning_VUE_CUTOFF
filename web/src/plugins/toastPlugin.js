import VueToastificationPlugin from 'vue-toastification'
// Import the CSS or use your own!
import 'vue-toastification/dist/index.css'

const options = {
  // You can set your default options here
  allowHtml: true
}

export default {
  install (Vue) {
    Vue.use(VueToastificationPlugin, options);
  }
}
