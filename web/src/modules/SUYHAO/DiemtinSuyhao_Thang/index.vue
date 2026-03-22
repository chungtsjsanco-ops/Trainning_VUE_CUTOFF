<template src="./index.template.html"></template>

<script>
export default {
  name: 'DiemTinSuyHao_Thang',
  data: () => ({
    filterDate: '',
    filterDate_view: '',
    filterDate_pre: '',
    filterDate_pre_view: '',
    loading: false,
    items: []
  }),
  computed: {},
  created() {
    const today = new Date()
    const yesterday = new Date(today)
    yesterday.setDate(today.getDate() - 1)
    const yyyy = yesterday.getFullYear()
    const mm = String(yesterday.getMonth() + 1).padStart(2, '0')
    const dd = String(yesterday.getDate()).padStart(2, '0')
    this.filterDate = `${yyyy}-${mm}-${dd}`
    const [year, month, day] = this.filterDate.split('-')
    this.filterDate_view = `${day}/${month}/${year}`

    const yesterday1 = new Date(yesterday)
    yesterday1.setDate(yesterday.getDate() - 1)
    const yyyy1 = yesterday1.getFullYear()
    const mm1 = String(yesterday1.getMonth() + 1).padStart(2, '0')
    const dd1 = String(yesterday1.getDate()).padStart(2, '0')
    this.filterDate_pre = `${yyyy1}-${mm1}-${dd1}`
    const [year1, month1, day1] = this.filterDate_pre.split('-')
    this.filterDate_pre_view = `${day1}/${month1}/${year1}`
  },
  methods: {
    mapItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        ALLCOUNT_MONTH: x.ALLCOUNT_MONTH,
        ALLCOUNT_PRE: x.ALLCOUNT_PRE,
        ALLCOUNT_TODAY: x.ALLCOUNT_TODAY,
        BADCOUNT_MONTH: x.BADCOUNT_MONTH,
        BADCOUNT_PRE: x.BADCOUNT_PRE,
        BADCOUNT_TODAY: x.BADCOUNT_TODAY,
        PERCENT_MONTH: x.PERCENT_MONTH,
        PERCENT_PRE: x.PERCENT_PRE,
        PERCENT_TODAY: x.PERCENT_TODAY,
        STT: x.STT,
        TANG_GIAM: x.TANG_GIAM,
        TTVT: x.TTVT
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
        const res = await this.$root.context.get('/api/suyhao/SOLIEU_SUYHAO_OLT_TONGHOP_NGAY', {
          prmNGAY: prmNGAY
        })
        const rawItems = (res && res.data) || []
        console.log(rawItems)
        this.items = this.mapItems(rawItems)
      } catch (e) {
        this.items = []
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
