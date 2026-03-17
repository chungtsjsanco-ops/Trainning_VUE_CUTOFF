'use strict'
module.exports = {
  NODE_ENV: '"production"',
  API: JSON.stringify(process.env.VUE_APP_API || 'http://172.22.192.1:8080')
}
