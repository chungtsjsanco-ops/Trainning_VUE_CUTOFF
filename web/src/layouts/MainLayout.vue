<template>
  <div class="main-wrapper">
    <b-navbar type="dark" variant="primary" class="px-4">
      <b-navbar-brand to="/" class="font-weight-bold">
        Training Web
      </b-navbar-brand>

      <b-navbar-nav>
        <b-nav-item to="/" exact>Trang chủ</b-nav-item>
        <b-nav-item to="/demo">Demo</b-nav-item>
        <b-nav-item-dropdown text="Thuê bao KLL">
          <b-dropdown-item to="/diemtin_tb_kll">
            Điểm tin thuê bao KLL
          </b-dropdown-item>
          <b-dropdown-item to="/diemtin_suyhao">
            Điểm tin suy hao
          </b-dropdown-item>
          <b-dropdown-item to="/ob_tb_kll">
            OB thuê bao KLL
          </b-dropdown-item>
           <b-dropdown-item to="/no_ob_tb_kll">
            Danh sách thuê bao không xác minh
          </b-dropdown-item>
        </b-nav-item-dropdown>
        <b-nav-item-dropdown text="Suy hao">
        <b-dropdown-item to="/diemtin_suyhao">
            Điểm tin suy hao
          </b-dropdown-item>
          <b-dropdown-item to="/diemtin_suyhao_thang">
            Điểm tin suy hao tổng hợp
          </b-dropdown-item>
          <b-dropdown-item to="diemtin_suyhao_tungay_denngay">
            Điểm tin suy hao theo thời gian
          </b-dropdown-item>
          <b-dropdown-item to="config_olt">
            Danh sách olt theo đơn vị
          </b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto align-items-center">
        <b-nav-item-dropdown right>
          <template #button-content>
            <span class="text-white">
              <i class="fa fa-user mr-1"></i> {{ userName }}
            </span>
          </template>
          <b-dropdown-item-button @click="doLogout">
            <i class="fa fa-sign-out mr-2"></i> Đăng xuất
          </b-dropdown-item-button>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-navbar>

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
  width: 100%;
}
</style>
