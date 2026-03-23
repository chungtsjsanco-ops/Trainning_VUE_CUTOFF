<template src="./index.template.html"></template>

<script>
export default {
  name: 'CONFIG_OLT',
  data: () => ({
    selected_Ds_donvi: null,
    service_Ds_donvi: [],

    selected_Ds_tram: null,
    service_Ds_tram: [],

    loading: false,
    items: []
  }),
  computed: {},
  created() {
    this.danhsach_donvi()
  },
  methods: {
    async danhsach_donvi() {
      this.loading = true
      try {
        const res = await this.$root.context.get('/api/suyhao/VIEW_DS_DONVI', {
          prmDk: 1,
          prmDONVI: ''
        })

        const items = (res && res.data) || []
        this.serviceOptions = items.map((x) => ({
          value: x.MA,
          text: x.TEN_MOI
        }))
      } catch (e) {
        this.$bvToast.toast('Không tải được danh sách đơn vị.', {
          title: 'Lỗi',
          variant: 'danger',
          autoHideDelay: 3000,
          toaster: 'b-toaster-bottom-right'
        })
      } finally {
        this.loading = false
      }
    },

    onServiceDonvi() {
      if (this.donviSelect) {
        this.danhsach_tram()
      }
    },

    danhsach_tram() {
      this.loading = true
      try {
        const res = this.$root.context.get('/api/suyhao/VIEW_DS_DONVI', {
          prmDk: 2,
          prmDONVI: this.donviSelect
        })

        const items = (res && res.data) || []
        this.serviceOptions = items.map((x) => ({
          value: x.MA,
          text: x.TEN_MOI
        }))
      } catch (e) {
        this.$bvToast.toast('Không tải được danh sách trạm.', {
          title: 'Lỗi',
          variant: 'danger',
          autoHideDelay: 3000,
          toaster: 'b-toaster-bottom-right'
        })
      } finally {
        this.loading = false
      }
    },

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
