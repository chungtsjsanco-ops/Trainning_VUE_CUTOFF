<template src="./index.template.html"></template>

<script>
export default {
  name: 'DiemTinKLL',
  data: () => ({
    filterType: 'detail',
    filterDate: '',
    loading: false,
    items: [],
    fields: []
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
  methods: {
    getDetailFields() {
      return [
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
    },

    getSummaryFields() {
      return [
        { key: 'index', label: '#', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'center', label: 'Trung tâm viễn thông' },
        { key: 'total', label: 'Tổng phiếu', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'khdn', label: 'KHDN', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'khcn', label: 'KH sử dụng bt', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'techError', label: 'Lỗi kỹ thuật', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'riskLeave', label: 'Nguy cơ rời mạng', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'noContact', label: 'Không liên lạc được', thClass: 'text-center', tdClass: 'text-center' }
      ]
    },

    mapDetailItems(apiItems) {
      if (!Array.isArray(apiItems)) {
        return []
      }

      return apiItems.map((x) => ({
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

    mapSummaryItems(apiItems) {
      if (!Array.isArray(apiItems)) {
        return []
      }

      return apiItems.map((x) => ({
        center: x.TEN_DV|| '',
        total: Number(x.TONG || 0),
        khdn: Number(x.KHDN || 0),
        khcn: Number(x.BINHTHUONG || 0),
        techError: Number(x.LOI_KT || 0),
        riskLeave: Number(x.NGUYCO || 0),
        noContact: Number(x.KLL || 0)
      }))
    },

    async onSearch() {
      if (!this.filterDate) {
        return
      }

      this.loading = true
      this.items = []

      const prmDK = this.filterType === 'detail' ? 1 : 2

      // Chuyển yyyy-MM-dd từ input date sang chuỗi dd/MM/yyyy cho API
      const [year, month, day] = this.filterDate.split('-')
      const prmNGAY = `${day}/${month}/${year}`

      try {
        const res = await this.$root.context.get('/api/thuebao_kll/SOLIEU_KLL_VIEW', {
          prmDK,
          prmNGAY
        })

        const rawItems = (res && res.data) || []

        if (this.filterType === 'detail') {
          this.fields = this.getDetailFields()
          this.items = this.mapDetailItems(rawItems)
        } else {
          this.fields = this.getSummaryFields()
          this.items = this.mapSummaryItems(rawItems)
        }
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

