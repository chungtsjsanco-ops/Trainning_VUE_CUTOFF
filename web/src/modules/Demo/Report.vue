<template>
  <div class="report-page container py-4">
    <h4 class="mb-3">Tạo báo cáo dịch vụ</h4>

    <div class="report-filters">
        <b-form-group label="Chọn dịch vụ" label-for="serviceSelect">
          <b-form-select
            id="serviceSelect"
            v-model="selectedService"
            :options="serviceOptions"
            :disabled="loading"
            @change="onServiceChange"
          >
            <template #first>
              <b-form-select-option :value="null" disabled>-- Chọn dịch vụ --</b-form-select-option>
            </template>
          </b-form-select>
        </b-form-group>

      <div class="report-filter-col">
        <b-form-group label="Loại hình thuê bao" label-for="loaiTbSelect">
          <b-form-select
            id="loaiTbSelect"
            v-model="selectedLoaiTB"
            :options="loaiTbOptions"
            :disabled="loadingLoaiTb || !selectedService"
          >
            <template #first>
              <b-form-select-option :value="null" disabled>
                -- Chọn loại hình --
              </b-form-select-option>
            </template>
          </b-form-select>
        </b-form-group>
      </div>
    </div>

    <div class="mt-3">
      <b-button
        variant="primary"
        :disabled="!selectedService || !selectedLoaiTB || loadingReport"
        @click="generateReport"
      >
        Tạo báo cáo
      </b-button>
    </div>

    <div class="mt-4">
      <b-overlay :show="loadingReport" rounded="sm" opacity="0.15">
        <b-table
          v-if="reportRows.length"
          small
          bordered
          hover
          responsive
          :items="reportRows"
          :fields="reportFields"
        />

        <b-alert v-else show variant="secondary" class="mb-0">
          Chưa có dữ liệu. Hãy chọn dịch vụ + loại hình và bấm “Tạo báo cáo”.
        </b-alert>
      </b-overlay>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DemoReport',
  data: () => ({
    loading: false,
    selectedService: null,
    serviceOptions: [],

    loadingLoaiTb: false,
    selectedLoaiTB: null,
    loaiTbOptions: [],

    loadingReport: false,
    reportRows: [],
    reportFields: [
      { key: 'THUEBAO_ID', label: 'Thuê bao ID' },
      { key: 'MA_TB', label: 'Mã TB' },
      { key: 'TEN_TB', label: 'Tên TB' },
      { key: 'DIACHI_TB', label: 'Địa chỉ TB' },
      { key: 'LOAITB_ID', label: 'Loại TB' },
      { key: 'DICHVUVT_ID', label: 'Dịch vụ' }
    ]
  }),
  created() {
    // NOTE: Khi mở tab báo cáo -> tự động gọi API lấy danh sách dịch vụ
    this.fetchServices()
  },
  methods: {
    async fetchServices() {
      this.loading = true
      try {
        const res = await this.$root.context.get('/api/sample/dichvu_vt', {})
        const items = (res && res.data) || []
        this.serviceOptions = items.map((x) => ({
          value: x.DICHVUVT_ID,
          text: x.TEN_DVVT
        }))
      } catch (e) {
        this.$bvToast.toast('Không tải được danh sách dịch vụ.', {
          title: 'Lỗi',
          variant: 'danger',
          autoHideDelay: 3000,
          toaster: 'b-toaster-bottom-right'
        })
      } finally {
        this.loading = false
      }
    },

    onServiceChange() {
      this.selectedLoaiTB = null
      this.loaiTbOptions = []
      this.reportRows = []

      if (this.selectedService) {
        this.fetchLoaiTb()
      }
    },

    async fetchLoaiTb() {
      this.loadingLoaiTb = true
      try {
        //const res = await this.$root.context.get('/api/sample/loaihinh_tb', {
        //params: {
        //loai: this.selectedService
        //}
        //})
        const res = await this.$root.context.get('/api/sample/loaihinh_tb?loai=' + this.selectedService)
        const items = (res && res.data) || []
        this.loaiTbOptions = items.map((x) => ({
          value: x.LOAITB_ID,
          text: x.LOAIHINH_TB
        }))
      } catch (e) {
        this.$bvToast.toast('Không tải được loại hình thuê bao.', {
          title: 'Lỗi',
          variant: 'danger',
          autoHideDelay: 3000
        })
      } finally {
        this.loadingLoaiTb = false
      }
    },

    async generateReport() {
      if (!this.selectedService || !this.selectedLoaiTB) return

      this.loadingReport = true
      this.reportRows = []
      try {
        const url =
          '/api/sample/ds_thuebao?DICHVUVT_ID=' +
          this.selectedService +
          '&LOAIHINH_TB=' +
          this.selectedLoaiTB

        const res = await this.$root.context.get(url)
        const items = (res && res.data) || []
        this.reportRows = Array.isArray(items) ? items : []
      } catch (e) {
        this.$bvToast.toast('Không tạo được báo cáo (không tải được dữ liệu).', {
          title: 'Lỗi',
          variant: 'danger',
          autoHideDelay: 3000,
          toaster: 'b-toaster-bottom-right'
        })
      } finally {
        this.loadingReport = false
      }
    }
  }
}
</script>

<style scoped>
.report-filters {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 16px;
}

/* tương đương col-3 (3/12) */
.report-filter-col {
  flex: 0 0 25%;
  max-width: 25%;
  min-width: 240px;
}
</style>
