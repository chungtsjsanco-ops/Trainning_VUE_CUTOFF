'use strict'
module.exports = {
  NODE_ENV: '"production"',
  API: JSON.stringify(process.env.VUE_APP_API || 'http://10.47.200.62:8080')
}
