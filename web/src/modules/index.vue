<template>
  <div>
    <h4 class="mb-4">Trang chủ</h4>
    <div class="row">
      <div class="col-md-6 col-lg-4 mb-4">
        <div class="card shadow-sm">
          <div class="card-body">
            <h6 class="card-title text-muted">Tài khoản</h6>
            <p class="card-text fw-bold fs-5">{{ userName }}</p>
          </div>
        </div>
      </div>
      <div class="col-md-6 col-lg-4 mb-4">
        <div class="card shadow-sm">
          <div class="card-body">
            <h6 class="card-title text-muted">Vai trò</h6>
            <p class="card-text">
              <b-badge v-for="role in roles" :key="role" variant="primary" class="mr-1">{{ role }}</b-badge>
              <span v-if="!roles || roles.length === 0" class="text-muted">Không có</span>
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="card shadow-sm mt-2">
      <div class="card-body">
        <h6 class="card-title">Ví dụ gọi API có xác thực</h6>
        <p class="text-muted small">Nhấn nút bên dưới để thực hiện một API call mẫu (có kèm JWT token)</p>
        <b-button variant="primary" size="sm" :disabled="loadingApi" @click="callSampleApi">
          <b-spinner small v-if="loadingApi" class="mr-1" />
          Gọi API mẫu
        </b-button>
        <pre v-if="apiResult" class="mt-3 p-3 bg-light rounded small">{{ apiResult }}</pre>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Home',
  data: () => ({
    loadingApi: false,
    apiResult: null
  }),
  computed: {
    userName() {
      return this.$root.context.user.getUserName() || 'N/A'
    },
    roles() {
      return this.$root.context.user.getRoles() || []
    }
  },
  methods: {
    async callSampleApi() {
      this.loadingApi = true
      this.apiResult = null
      try {
        const result = await this.$root.context.get('/api/sample', {})
        this.apiResult = JSON.stringify(result, null, 2)
      } catch (error) {
        this.apiResult = JSON.stringify({ error: 'Không thể kết nối API. Vui lòng kiểm tra backend.' }, null, 2)
      } finally {
        this.loadingApi = false
      }
    }
  }
}
</script>
