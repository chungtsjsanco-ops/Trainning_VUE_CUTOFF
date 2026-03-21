'use strict'
module.exports = {
  NODE_ENV: '"production"',
  API: JSON.stringify(process.env.VUE_APP_API || 'http://localhost:8080')
}
