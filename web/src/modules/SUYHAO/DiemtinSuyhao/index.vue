<template src="./index.template.html"></template>

<script>
export default {
  name: 'DiemTinSuyHao',
  data: () => ({
    filterType: '1',
    filterDate: '',
    loading: false,
    items_olt: [],
    items_chitiet: [],

    currentPage: 1,
    perPage: 20
  }),
  computed: {
    sumTotalBad() {
      return this.items_olt.reduce((sum, items_olt) => sum + Number(items_olt.totalBad || 0), 0)
    },
    sumTotal() {
      return this.items_olt.reduce((sum, items_olt) => sum + Number(items_olt.total || 0), 0)
    },
    paginatedOlt() {
      const start = (this.currentPage - 1) * this.perPage
      const end = start + this.perPage
      return this.items_olt.slice(start, end)
    },
    paginatedChitiet() {
      const start = (this.currentPage - 1) * this.perPage
      const end = start + this.perPage
      return this.items_chitiet.slice(start, end)
    }
  },
  created() {
    const today = new Date()
    const yesterday = new Date(today)
    yesterday.setDate(today.getDate() - 1)
    const yyyy = yesterday.getFullYear()
    const mm = String(yesterday.getMonth() + 1).padStart(2, '0')
    const dd = String(yesterday.getDate()).padStart(2, '0')
    this.filterDate = `${yyyy}-${mm}-${dd}`
  },
  methods: {
    mapItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        center: x.TTVT || '',
        station: x.PROVINCE_CODE || '',
        system: x.SYSTEMNAME || '',
        ipAddress: x.MANAGER_IP || '',
        totalBad: Number(x.BADCOUNT || 0),
        total: Number(x.ALLCOUNT || 0),
        badRate: x.PERCENT || 0
      }))
    },

    mapItems_ct(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        C2: x.C2 || '',
        C3: x.C3 || '',
        C4: x.C4 || '',
        SYSTEM: x.SYSTEM_NAME || '',
        STATUS: x.STATUS || '',
        ACCOUNTFIBER: x.ACCOUNTFIBER || ''
      }))
    },

    async onSearch() {
      this.loading = true
      this.currentPage = 1
      this.items_olt = []
      this.items_chitiet = []

      const [year, month, day] = this.filterDate.split('-')
      const prmNGAY = `${day}/${month}/${year}`
      try {
        if (this.filterType == 1) {
          const res = await this.$root.context.get('/api/suyhao/SOLIEU_SUYHAO_OLT_VIEWNGAY', {
            prmNGAY: prmNGAY
          })
          const rawItems = (res && res.data) || []
          this.items_olt = this.mapItems(rawItems)
        } else if (this.filterType == 2) {
          const res_ct = await this.$root.context.get('/api/suyhao/SOLIEU_SUYHAO_CTS_VIEWNGAY', {
            prmNGAY: prmNGAY
          })
          const rawItems_ct = (res_ct && res_ct.data) || []
          this.items_chitiet = this.mapItems_ct(rawItems_ct)
        }
      } catch (e) {
        this.items_olt = []
        this.items_chitiet = []
      } finally {
        this.loading = false
      }
    },

    onExport() {
      // TODO: gắn API/file export tổng hợp
    }
  }
}
</script>

<style src="./index.css"></style>
