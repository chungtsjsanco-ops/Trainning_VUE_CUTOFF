<template src='./index.template.html'></template>
<script>
export default {
  name: 'Login',
  data: () => ({
    username: '',
    password: '',
    loading: false,
    loginFailed: false
  }),
  created() {
    this.$root.context.logOut()
  },
  methods: {
    async btnLogin_Click() {
      this.loading = true
      this.loginFailed = false
      try {
        const response = await this.$root.context.postWithOutToken('/api/auth/login', {
          username: this.username,
          password: this.password
        })
        if (response && response.error === null && response.data && response.data.token) {
          this.$root.context.authenticate(response.data)
          this.$router.push({ name: 'Demo' })
        } else {
          this.loginFailed = true
        }
      } catch (error) {
        this.loginFailed = true
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 100%;
  max-width: 420px;
  border-radius: 12px;
}
</style>
