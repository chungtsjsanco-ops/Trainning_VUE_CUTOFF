<template>
  <div>
    <div class="demo-header mb-4 p-4 rounded-lg">
      <div class="d-flex align-items-center justify-content-between flex-wrap">
        <div>
          <h3 class="text-white mb-1">Chào mừng, {{ userName }}!</h3>
          <p class="text-white-50 mb-0">Hôm nay là {{ today }} — Chúc bạn một ngày làm việc hiệu quả</p>
        </div>
        <div class="mt-2 mt-md-0">
          <b-badge variant="light" class="px-3 py-2 text-primary font-weight-bold">
            <i class="fa fa-circle text-success mr-1"></i> Đang trực tuyến
          </b-badge>
        </div>
      </div>
    </div>

    <!-- Stat cards -->
    <div class="row mb-4">
      <div v-for="stat in stats" :key="stat.label" class="col-6 col-md-3 mb-3">
        <div class="card shadow-sm h-100 stat-card">
          <div class="card-body d-flex align-items-center">
            <div :class="`stat-icon bg-${stat.color}-light mr-3`">
              <i :class="`fa fa-${stat.icon} text-${stat.color}`"></i>
            </div>
            <div>
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-label text-muted">{{ stat.label }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <!-- Recent Activity -->
      <div class="col-lg-7 mb-4">
        <div class="card shadow-sm h-100">
          <div class="card-header bg-white d-flex align-items-center justify-content-between">
            <span class="font-weight-bold">Hoạt động gần đây</span>
            <b-badge variant="primary" pill>{{ activities.length }}</b-badge>
          </div>
          <div class="card-body p-0">
            <div
              v-for="(item, idx) in activities"
              :key="idx"
              class="activity-item d-flex align-items-start p-3"
              :class="{ 'border-top': idx > 0 }"
            >
              <div :class="`activity-dot bg-${item.color} mr-3 mt-1`"></div>
              <div class="flex-grow-1">
                <div class="font-weight-medium">{{ item.action }}</div>
                <div class="text-muted small">{{ item.time }}</div>
              </div>
              <b-badge :variant="item.color" class="align-self-center">{{ item.status }}</b-badge>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="col-lg-5 mb-4">
        <div class="card shadow-sm h-100">
          <div class="card-header bg-white font-weight-bold">Thao tác nhanh</div>
          <div class="card-body">
            <b-button
              v-for="action in quickActions"
              :key="action.label"
              :variant="action.variant"
              class="w-100 mb-2 text-left d-flex align-items-center"
              @click="handleAction(action)"
            >
              <i :class="`fa fa-${action.icon} mr-2`"></i>
              {{ action.label }}
            </b-button>
          </div>
        </div>
      </div>
    </div>

    <!-- Sample data table -->
    <div class="card shadow-sm">
      <div class="card-header bg-white d-flex align-items-center justify-content-between">
        <span class="font-weight-bold">Danh sách mẫu</span>
        <b-button variant="outline-primary" size="sm" @click="refreshTable">
          <b-spinner small v-if="tableLoading" class="mr-1" />
          <i v-else class="fa fa-refresh mr-1"></i>
          Làm mới
        </b-button>
      </div>
      <div class="card-body p-0">
        <b-table
          :items="tableItems"
          :fields="tableFields"
          hover
          responsive
          class="mb-0"
          :busy="tableLoading"
        >
          <template #table-busy>
            <div class="text-center my-3">
              <b-spinner variant="primary" />
            </div>
          </template>
          <template #cell(status)="{ value }">
            <b-badge :variant="value === 'Hoạt động' ? 'success' : value === 'Chờ duyệt' ? 'warning' : 'secondary'">
              {{ value }}
            </b-badge>
          </template>
          <template #cell(actions)>
            <b-button variant="outline-primary" size="sm" class="mr-1">
              <i class="fa fa-eye"></i>
            </b-button>
            <b-button variant="outline-secondary" size="sm">
              <i class="fa fa-edit"></i>
            </b-button>
          </template>
        </b-table>
      </div>
    </div>

    <b-modal
      v-model="showCreateUserModal"
      title="Thêm người dùng mới"
      hide-footer
      centered
      @hidden="onCreateUserModalHidden"
    >
      <UserCreateForm
        ref="userCreateForm"
        :busy="creatingUser"
        @submit="createUser"
        @cancel="showCreateUserModal = false"
      />
    </b-modal>
  </div>
</template>

<script>
import moment from 'moment'
import UserCreateForm from './UserCreateForm.vue'

export default {
  name: 'Demo',
  components: { UserCreateForm },
  data: () => ({
    tableLoading: false,
    showCreateUserModal: false,
    creatingUser: false,
    stats: [
      { label: 'Người dùng', value: '1,284', icon: 'users', color: 'primary' },
      { label: 'Đơn hàng', value: '356', icon: 'shopping-cart', color: 'success' },
      { label: 'Doanh thu', value: '48.2M', icon: 'money', color: 'warning' },
      { label: 'Yêu cầu mới', value: '12', icon: 'bell', color: 'danger' }
    ],
    activities: [
      { action: 'Người dùng mới đăng ký tài khoản', time: '2 phút trước', status: 'Mới', color: 'success' },
      { action: 'Đơn hàng #1042 được xác nhận', time: '15 phút trước', status: 'Xác nhận', color: 'primary' },
      { action: 'Báo cáo tháng 3 đã xuất xong', time: '1 giờ trước', status: 'Hoàn thành', color: 'info' },
      { action: 'Cập nhật hệ thống lên v2.1.0', time: '3 giờ trước', status: 'Triển khai', color: 'warning' },
      { action: 'Sao lưu dữ liệu hàng tuần', time: 'Hôm qua', status: 'Thành công', color: 'secondary' }
    ],
    quickActions: [
      { label: 'Thêm người dùng mới', icon: 'user-plus', variant: 'outline-primary' },
      { label: 'Tạo báo cáo', icon: 'file-text-o', variant: 'outline-success' },
      { label: 'Xem nhật ký hệ thống', icon: 'list-alt', variant: 'outline-info' },
      { label: 'Cài đặt hệ thống', icon: 'cogs', variant: 'outline-secondary' }
    ],
    tableFields: [
      { key: 'id', label: 'ID', thStyle: { width: '60px' } },
      { key: 'name', label: 'Tên' },
      { key: 'email', label: 'Email' },
      { key: 'role', label: 'Vai trò' },
      { key: 'status', label: 'Trạng thái' },
      { key: 'actions', label: 'Thao tác', thStyle: { width: '100px' } }
    ],
    tableItems: [
      { id: 1, name: 'Nguyễn Văn A', email: 'vana@example.com', role: 'Admin', status: 'Hoạt động' },
      { id: 2, name: 'Trần Thị B', email: 'thib@example.com', role: 'User', status: 'Hoạt động' },
      { id: 3, name: 'Lê Văn C', email: 'vanc@example.com', role: 'Editor', status: 'Chờ duyệt' },
      { id: 4, name: 'Phạm Thị D', email: 'thid@example.com', role: 'User', status: 'Vô hiệu' },
      { id: 5, name: 'Hoàng Văn E', email: 'vane@example.com', role: 'Viewer', status: 'Hoạt động' }
    ]
  }),
  computed: {
    userName() {
      return this.$root.context.user.getUserName() || 'Người dùng'
    },
    today() {
      return moment().format('dddd, DD/MM/YYYY')
    }
  },
  methods: {
    async refreshTable() {
      this.tableLoading = true
      await new Promise(resolve => setTimeout(resolve, 800))
      this.tableLoading = false
      this.$bvToast.toast('Danh sách đã được làm mới!', {
        title: 'Thông báo',
        variant: 'success',
        autoHideDelay: 2500,
        toaster: 'b-toaster-bottom-right'
      })
    },
    handleAction(action) {
      // NOTE: Tùy theo label quick action mà điều hướng chức năng khác nhau
      if (action.label === 'Thêm người dùng mới') {
        this.showCreateUserModal = true
        return
      }
      if (action.label === 'Tạo báo cáo') {
        this.handleCreateReport()
        return
      }

      this.$bvToast.toast(`Chức năng "${action.label}" đang được phát triển.`, {
        title: 'Demo',
        variant: 'info',
        autoHideDelay: 2500,
        toaster: 'b-toaster-bottom-right'
      })
    },
    async createUser(payload) {
      this.creatingUser = true
      try {
        await new Promise(resolve => setTimeout(resolve, 600))
        this.showCreateUserModal = false
        this.$bvToast.toast(`Đã tạo người dùng "${payload.name}"`, {
          title: 'Thành công',
          variant: 'success',
          autoHideDelay: 2500,
          toaster: 'b-toaster-bottom-right'
        })
      } finally {
        this.creatingUser = false
      }
    },
    onCreateUserModalHidden() {
      const form = this.$refs.userCreateForm
      if (form && typeof form.reset === 'function') form.reset()
    },
    handleCreateReport() {
      // NOTE: Mở màn hình báo cáo (DemoReport) ở TAB mới của trình duyệt
      const route = this.$router.resolve({ name: 'DemoReport' })
      window.open(route.href, '_blank')
    }
  }
}
</script>

<style scoped>
.demo-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.stat-card {
  border: none;
  border-radius: 10px;
  transition: transform 0.15s;
}
.stat-card:hover {
  transform: translateY(-2px);
}
.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}
.bg-primary-light { background-color: #e8eeff; }
.bg-success-light { background-color: #e6f9f0; }
.bg-warning-light { background-color: #fff8e6; }
.bg-danger-light  { background-color: #ffeaea; }
.bg-info-light    { background-color: #e6f6ff; }
.stat-value {
  font-size: 1.4rem;
  font-weight: 700;
  line-height: 1.2;
}
.stat-label {
  font-size: 0.78rem;
}
.activity-item {
  transition: background 0.1s;
}
.activity-item:hover {
  background-color: #f8f9fa;
}
.activity-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}
.font-weight-medium {
  font-weight: 500;
}
</style>
