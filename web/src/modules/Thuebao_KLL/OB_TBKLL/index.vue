<template src="./index.template.html"></template>

<script>
import KLLNoObForm from '../KLLNoObForm.vue'
import KLLObForm from '../KLLObForm.vue'

export default {
  name: 'OB_TBKLL',
  components: {
    KLLNoObForm,
    KLLObForm
  },
  data: () => ({
    filterType: 'detail',
    filterDate: '',
    loading: false,
    items: [],
    selectedRecord: null,
    showNoObModal: false,
    showObModal: false,
    fields: [
      { key: 'index', label: '#', thClass: 'text-center', tdClass: 'text-center' },
      { key: 'center', label: 'Trung tâm viễn thông' },
      { key: 'area', label: 'Địa bàn' },
      { key: 'fiber', label: 'Fiber/MyTV' },
      { key: 'subscriberName', label: 'Tên thuê bao' },
      { key: 'installAddress', label: 'Địa chỉ lắp đặt' },
      { key: 'phone', label: 'SDT' },
      { key: 'nvob', label: 'NVOB' },
      { key: 'result', label: 'Kết quả' },
      { key: 'note', label: 'Ghi chú' }
    ]
  }),
  created() {
    // Mặc định là ngày D-1 (hôm qua) cho input date (yyyy-MM-dd)
    const today = new Date()
    const yesterday = new Date(today)
    yesterday.setDate(today.getDate() - 1)
    const yyyy = yesterday.getFullYear()
    const mm = String(yesterday.getMonth() + 1).padStart(2, '0')
    const dd = String(yesterday.getDate()).padStart(2, '0')
    this.filterDate = `${yyyy}-${mm}-${dd}`
  },
  computed: {
    prmNgayForOb() {
      if (!this.filterDate) return null
      const [year, month, day] = this.filterDate.split('-')
      return `${day}/${month}/${year}`
    },
    prmNguoiCapnhatForOb() {
      return (
        (this.$root.context &&
          this.$root.context.user &&
          this.$root.context.user.getUserName &&
          this.$root.context.user.getUserName()) ||
        'admin.hue'
      )
    }
  },
  methods: {
    mapApiItems(apiItems) {
      if (!Array.isArray(apiItems)) {
        return []
      }

      return apiItems.map((x) => ({
        // giữ lại ID và mã TB để gửi cho proc insert
        thuebaoId: x.ID || x.THUEBAO_ID || null,
        maTB: x.MA_TB || x.FIBER || '',
        center: x.C2 || '',
        area: x.C4 || '',
        fiber: [x.FIBER, x.MYTV].filter(Boolean).join(' / '),
        subscriberName: x.TEN_TB || '',
        installAddress: x.DIACHI_LD || '',
        phone: x.SDT || x.DIENTHOAI || '',
        nvob: x.NVOB || '',
        result: x.TRANGTHAI_TB || '',
        note: x.GHI_CHU || ''
      }))
    },

    openNoObModal(item) {
      this.selectedRecord = item
      this.showNoObModal = true
      console.log(this.selectedRecord);
    },

    openObModal(item) {
      this.selectedRecord = item
      this.showObModal = true
    },

    async onSearch(payload) {
      // payload có thể được truyền từ form con (NoOb / Ob) với prmNGAY, prmNGUOI_CAPNHAT
      const overrideNgay = payload && payload.prmNGAY
      const overrideNguoi = payload && payload.prmNGUOI_CAPNHAT

      if (!this.filterDate && !overrideNgay) {
        return
      }

      this.loading = true
      this.items = []

      let prmNGAY = overrideNgay
      let prmNGUOI_CAPNHAT = overrideNguoi

      if (!prmNGAY) {
        const [year, month, day] = this.filterDate.split('-')
        prmNGAY = `${day}/${month}/${year}`
      }

      if (!prmNGUOI_CAPNHAT) {
        prmNGUOI_CAPNHAT = this.prmNguoiCapnhatForOb
      }
      try {
        const res = await this.$root.context.get('/api/thuebao_kll/SOLIEU_KLL_VIEW_OB', {
          prmNGAY,
          prmNGUOI_CAPNHAT
        })

        const rawItems = (res && res.data) || []
        this.items = this.mapApiItems(rawItems)
      } catch (e) {
        this.items = []
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style src="./index.css"></style>

