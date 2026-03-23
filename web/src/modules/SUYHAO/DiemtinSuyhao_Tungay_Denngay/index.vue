<template src="./index.template.html"></template>

<script>
export default {
  name: 'DiemTinSuyHao_TUNGAY_DENNGAY',
  data: () => ({
    filterDate: '',
    filterDate_view: '',
    filterDate_2: '',
    filterDate_view_2: '',
    loading: false,
    items: []
  }),
  computed: {},
  created() {
    const today = new Date()
    const yesterday = new Date(today)
    yesterday.setDate(today.getDate() - 1)
    const yyyy2 = yesterday.getFullYear()
    const mm2 = String(yesterday.getMonth() + 1).padStart(2, '0')
    const dd2 = String(yesterday.getDate()).padStart(2, '0')
    this.filterDate_2 = `${yyyy2}-${mm2}-${dd2}`
    const [year2, month2, day2] = this.filterDate_2.split('-')
    this.filterDate_view_2 = `${day2}/${month2}/${year2}`

    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1)
    const yyyy = firstDay.getFullYear()
    const mm = String(firstDay.getMonth() + 1).padStart(2, '0')
    const dd = String(firstDay.getDate()).padStart(2, '0')
    this.filterDate = `${yyyy}-${mm}-${dd}`
    const [year, month, day] = this.filterDate.split('-')
    this.filterDate_view = `${day}/${month}/${year}`
  },
  methods: {
    mapItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        ALLCOUNT_MONTH: x.ALLCOUNT_MONTH || '',
        ALLCOUNT_PRE: x.ALLCOUNT_PRE || '',
        ALLCOUNT_TODAY: x.ALLCOUNT_TODAY || '',
        BADCOUNT_MONTH: x.BADCOUNT_MONTH || '',
        BADCOUNT_PRE: x.BADCOUNT_PRE || '',
        BADCOUNT_TODAY: x.BADCOUNT_TODAY || '',
        PERCENT_MONTH: x.PERCENT_MONTH || '',
        PERCENT_PRE: x.PERCENT_PRE || '',
        PERCENT_TODAY: x.PERCENT_TODAY || '',
        STT: x.STT,
        TANG_GIAM: x.TANG_GIAM,
        TTVT: x.TTVT
      }))
    },

    async onSearch() {
      this.loading = true
      this.currentPage = 1
      this.items = []

      const [year, month, day] = this.filterDate.split('-')
      const prmTUNGAY = `${day}/${month}/${year}`

      const [year1, month1, day1] = this.filterDate_2.split('-')
      const prmDENNGAY = `${day1}/${month1}/${year1}`

      try {
        const res = await this.$root.context.get('/api/suyhao/SOLIEU_SUYHAO_OLT_TONGHOP_TUNGAY_DENNGAY', {
          prmTUNGAY: prmTUNGAY,
          prmDENNGAY: prmDENNGAY
        })
        const rawItems = (res && res.data) || []
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
