<template>
  <div class="main-wrapper">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary px-4">
      <span class="navbar-brand fw-bold">Training Web</span>
      <div class="ms-auto d-flex align-items-center">
        <span class="text-white me-3">
          <i class="fa fa-user mr-1"></i> {{ userName }}
        </span>
        <button class="btn btn-outline-light btn-sm" @click="doLogout">Đăng xuất</button>
      </div>
    </nav>
    <div class="content-wrapper p-4">
      <router-view />
    </div>
  </div>
</template>
<script>
export default {
  name: 'MainLayout',
  computed: {
    userName() {
      return this.$root.context.user.getUserName() || 'Người dùng'
    }
  },
  created() {
    if (!this.$root.context.isAuthenticated()) {
      this.$router.push({ name: 'Login' })
    }
  },
  methods: {
    doLogout() {
      this.$root.context.logOut()
      this.$router.push({ name: 'Login' })
    }
  }
}
</script>
<style scoped>
.main-wrapper {
  min-height: 100vh;
  background-color: #f8f9fa;
}
.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
