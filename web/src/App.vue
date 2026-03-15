<template>
  <!-- Code cũ:
  <div id="app">
    <router-view :key="$route.fullPath"></router-view>
  </div>
  -->

  <div id="app">
    <b-navbar
      v-if="$route.name !== 'Login'"
      type="dark"
      variant="primary"
    >
      <b-navbar-brand href="#">My App</b-navbar-brand>
      <b-navbar-nav>
        <b-nav-item to="/" exact tag="router-link">Trang chủ</b-nav-item>
        <b-nav-item to="/demo" tag="router-link">Demo</b-nav-item>
        <b-nav-item-dropdown text="Thuê bao KLL">
          <b-dropdown-item to="/diemtin_tb_kll" tag="router-link">
            Điểm tin thuê bao KLL
          </b-dropdown-item>
            <b-dropdown-item to="/ob_tb_kll" tag="router-link">
            OB thuê bao KLL
          </b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-navbar>
    <b-container class="mt-3">
      <router-view />
    </b-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
