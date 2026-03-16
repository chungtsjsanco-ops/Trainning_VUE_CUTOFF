<template>
  <b-form @submit.prevent="onSubmit">
    <div class="row">
      <div class="col-md-6">
        <b-form-group label="Trung tâm viễn thông">
          <b-form-input v-model="form.center" readonly />
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Địa bàn">
          <b-form-input v-model="form.area" readonly />
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Tên thuê bao">
          <b-form-input v-model="form.subscriberName" readonly />
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Địa chỉ lắp đặt">
          <b-form-input v-model="form.installAddress" readonly />
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Acc Fiber/MyTV">
          <b-form-input v-model="form.fiber" readonly />
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Kết quả OB *">
          <b-form-select v-model="form.obResult" @change="onServiceChange" @input="onServiceChange">
            <b-form-select-option :value="null" disabled>--- Chọn ---</b-form-select-option>
            <b-form-select-option value="1">OB thành công</b-form-select-option>
            <b-form-select-option value="2">OB không thành công</b-form-select-option>
          </b-form-select>
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Kết quả OB chi tiết *">
          <b-form-select v-model="selected_Ds_lydo" :options="service_Ds_lydo">
            <template #first>
              <b-form-select-option :value="null" disabled>-- Chọn lý do --</b-form-select-option>
            </template>
          </b-form-select>
        </b-form-group>
      </div>
      <div class="col-md-12">
        <b-form-group label="Ghi chú">
          <b-form-textarea v-model="form.note" rows="3" />
        </b-form-group>
      </div>
    </div>
    <div class="text-right mt-3">
      <b-button variant="secondary" class="mr-2" @click="$emit('close')">Đóng</b-button>
      <b-button type="submit" variant="primary">Lưu</b-button>
    </div>
  </b-form>
</template>

<script>
export default {
  name: 'KLLObForm',
  props: {
    record: {
      type: Object,
      default: null
    },
    prmNgay: {
      type: String,
      default: null
    },
    prmNguoiCapnhat: {
      type: String,
      default: null
    }
  },
  data: () => ({
    selected_Ds_lydo: null,
    service_Ds_lydo: [],
    form: {
      center: '',
      area: '',
      subscriberName: '',
      installAddress: '',
      fiber: '',
      obResult: null,
      obResultDetail: null,
      note: ''
    }
  }),
  watch: {
    record: {
      immediate: true,
      handler(newVal) {
        if (!newVal) return
        this.form.center = newVal.center || ''
        this.form.area = newVal.area || ''
        this.form.subscriberName = newVal.subscriberName || ''
        this.form.installAddress = newVal.installAddress || ''
        this.form.fiber = newVal.fiber || ''
      }
    }
  },
  methods: {
    async fetchServices(dk) {
      this.loading = true
      try {
        const res = await this.$root.context.get('/api/thuebao_kll/LYDO_OB_VIEW', {})
        const items = (res && res.data) || []
        const filtered = items.filter((x) => x.LOAI == dk)
        this.service_Ds_lydo = filtered.map((x) => ({
          value: x.ID_LYDO,
          text: x.NOIDUNG_OB
        }))
      } catch (e) {
        this.$bvToast.toast('Không tải được danh sách lý do.', {
          title: 'Lỗi',
          variant: 'danger',
          autoHideDelay: 3000,
          toaster: 'b-toaster-bottom-right'
        })
      } finally {
        this.loading = false
      }
    },
    onServiceChange(value) {
      if (value != null) {
        this.fetchServices(value)
      } else {
        this.service_Ds_lydo = []
        this.selected_Ds_lydo = null
      }
    },
    async onSubmit() {
      if (!this.record) {
        return
      }

      if (!this.form.obResult || !this.form.obResultDetail) {
        this.$bvToast &&
          this.$bvToast.toast('Vui lòng chọn đầy đủ Kết quả OB và Kết quả OB chi tiết.', {
            title: 'Thiếu thông tin',
            variant: 'warning',
            autoHideDelay: 3000,
            toaster: 'b-toaster-bottom-right'
          })
        return
      }

      const jsonData = {
        ID: this.record.thuebaoId,
        ID_LYDO: this.form.obResultDetail || this.form.obResult,
        GHICHU: this.form.note || ''
      }

      const prmJsonData = JSON.stringify(jsonData)

      try {
        const res = await this.$root.context.post('/api/thuebao_kll/SOLIEU_TB_KLL_2_INS', {
          prmJsonData
        })

        // Thành công (error_code = 00000000 => error = "200")
        if (res && (res.error === '200' || res.error === 200)) {
          this.$bvToast &&
            this.$bvToast.toast('Đã lưu kết quả OB thuê bao KLL.', {
              title: 'Thành công',
              variant: 'success',
              autoHideDelay: 2500,
              toaster: 'b-toaster-bottom-right'
            })
          // Trả ngược lại tham số ngày / người cho form cha dùng reload
          this.$emit('saved', {
            prmNGAY: this.prmNgay || null,
            prmNGUOI_CAPNHAT: this.prmNguoiCapnhat || null
          })
          this.$emit('close')
        } else {
          const msg = (res && (res.message_detail || res.message)) || 'Không lưu được kết quả OB thuê bao KLL.'

          this.$bvToast &&
            this.$bvToast.toast(msg, {
              title: 'Lỗi',
              variant: 'danger',
              autoHideDelay: 4000,
              toaster: 'b-toaster-bottom-right'
            })
        }
      } catch (e) {
        const resp = e && (e.response || e.res)
        const data = resp && resp.data
        const msg = (data && (data.message_detail || data.message)) || 'Không kết nối được máy chủ để lưu kết quả OB.'

        this.$bvToast &&
          this.$bvToast.toast(msg, {
            title: 'Lỗi hệ thống',
            variant: 'danger',
            autoHideDelay: 4000,
            toaster: 'b-toaster-bottom-right'
          })
      }
    }
  }
}
</script>
