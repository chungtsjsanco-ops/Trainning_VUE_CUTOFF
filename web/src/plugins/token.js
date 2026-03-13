import jwtdecode from 'jwt-decode'

const TOKEN_KEY = 'app-token'

export default {
  setToken(tokenData) {
    try {
      localStorage.setItem(TOKEN_KEY, JSON.stringify(tokenData))
      return true
    } catch (e) {
      return false
    }
  },
  getTokenFromStorage() {
    try {
      return JSON.parse(localStorage.getItem(TOKEN_KEY))
    } catch (e) {
      return null
    }
  },
  getToken() {
    try {
      const t = this.getTokenFromStorage()
      return t ? t.token : null
    } catch (e) {
      return null
    }
  },
  getTokenType() {
    try {
      const t = this.getTokenFromStorage()
      return t ? (t.token_type || 'Bearer') : null
    } catch (e) {
      return null
    }
  },
  getAuthorization() {
    const type = this.getTokenType()
    const value = this.getToken()
    if (type && value) {
      return `${type} ${value}`
    }
    return null
  },
  getClaims() {
    try {
      const token = this.getToken()
      if (token) return jwtdecode(token)
      return null
    } catch (e) {
      return null
    }
  },
  getProperty(name) {
    const claims = this.getClaims()
    return claims ? claims[name] : null
  },
  getUserName() {
    try {
      const t = this.getTokenFromStorage()
      if (t && t.username) return t.username
      return this.getProperty('sub')
    } catch (e) {
      return null
    }
  },
  getRoles() {
    try {
      const t = this.getTokenFromStorage()
      if (t && t.roles) return t.roles
      return this.getProperty('roles') || []
    } catch (e) {
      return []
    }
  },
  destroy() {
    try {
      localStorage.removeItem(TOKEN_KEY)
      return true
    } catch (e) {
      return false
    }
  }
}
