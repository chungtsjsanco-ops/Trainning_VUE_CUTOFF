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
        <b-form-group label="Mã thuê bao">
          <b-form-input v-model="form.maTB" readonly />
        </b-form-group>
      </div>
      <div class="col-md-6">
        <b-form-group label="Người đề nghị">
          <b-form-input v-model="form.NGUOI_DE_NGHI" />
        </b-form-group>
      </div>
      <div class="col-md-12">
        <b-form-group label="Nguyên nhân">
          <b-form-textarea v-model="form.NGUYENNHAN" rows="3" />
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
  name: 'KLLNoObForm',
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
    form: {
      center: '',
      area: '',
      subscriberName: '',
      installAddress: '',
      maTB: '',
      NGUOI_DE_NGHI: '',
      NGUYENNHAN: ''
    }
  }),
  watch: {
    record: {
      immediate: true,
      handler(newVal) {
        if (!newVal) return
        // Thông tin hiển thị
        this.form.center = newVal.center || ''
        this.form.area = newVal.area || ''
        this.form.subscriberName = newVal.subscriberName || ''
        this.form.installAddress = newVal.installAddress || ''
        this.form.maTB = newVal.maTB || newVal.fiber || ''
      }
    }
  },
  methods: {
    async onSubmit() {
      if (!this.record) {
        return
      }

      const jsonData = {
        THUEBAO_ID: this.record.thuebaoId,
        MA_TB: this.record.maTB || this.form.maTB || '',
        NGUOI_DE_NGHI: this.form.NGUOI_DE_NGHI || '',
        NGUYENNHAN: this.form.NGUYENNHAN || ''
      }

      const prmJsonData = JSON.stringify(jsonData)
      const prmNGUOI_CAPNHAT =
        this.prmNguoiCapnhat ||
        (this.$root.context &&
          this.$root.context.user &&
          this.$root.context.user.getUserName &&
          this.$root.context.user.getUserName()) ||
        'SYSTEM'

      try {
        const res = await this.$root.context.post(
          '/api/thuebao_kll/DANHSACH_TB_KHONG_OB_INS',
          {
            prmJsonData,
            prmNGUOI_CAPNHAT
          }
        )

        // Backend quy ước error = "200" là OK, còn lại là lỗi nghiệp vụ
        if (res && (res.error === '200' || res.error === 200)) {
          this.$bvToast &&
            this.$bvToast.toast('Đã lưu thuê bao KLL không cần OB.', {
              title: 'Thành công',
              variant: 'success',
              autoHideDelay: 2500,
              toaster: 'b-toaster-bottom-right'
            })
          this.$emit('saved', {
            prmNGAY: this.prmNgay || null,
            prmNGUOI_CAPNHAT: prmNGUOI_CAPNHAT || null
          })
          this.$emit('close')
        } else {
          const msg =
            (res && (res.message_detail || res.message)) ||
            'Không lưu được thông tin không cần OB.'

          this.$bvToast &&
            this.$bvToast.toast(msg, {
              title: 'Lỗi',
              variant: 'danger',
              autoHideDelay: 4000,
              toaster: 'b-toaster-bottom-right'
            })
        }
      } catch (e) {
        // Trường hợp backend trả HTTP 400/500, lấy message_detail trong body nếu có
        const resp = e && (e.response || e.res)
        const data = resp && resp.data
        const msg =
          (data && (data.message_detail || data.message)) ||
          'Không kết nối được máy chủ để lưu thông tin không OB.'

        this.$bvToast &&
          this.$bvToast.toast(msg, {
            title: 'Lỗi',
            variant: 'danger',
            autoHideDelay: 4000,
            toaster: 'b-toaster-bottom-right'
          })
      }
    }
  }
}
</script>

