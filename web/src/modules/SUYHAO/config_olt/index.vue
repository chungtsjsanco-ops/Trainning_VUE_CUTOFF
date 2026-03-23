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
    loading_tram: false,
    items: [],
    items_tram: []
  }),
  computed: {},
  created() {
    this.danhsach_donvi()
  },
  methods: {
     getDetailFields() {
      return [
        { key: 'index', label: '#', thClass: 'text-center', tdClass: 'text-center' },
        { key: 'TEN_DV_BK', label: 'Trung tâm viễn thông' },
        { key: 'TO_KYTHUAT', label: 'Tổ kỹ thuật' },
        { key: 'SYSTEMNAME', label: 'SYSTEMNAME' },
        { key: 'note', label: '' }
      ]
    },

    async danhsach_donvi() {
      this.loading = true
      try {
        const res = await this.$root.context.get('/api/suyhao/VIEW_DS_DONVI', {
          prmDK: 1,
          prmDONVI: null
        })

        const items = (res && res.data) || []
        this.service_Ds_donvi = items.map((x) => ({
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
      if (this.selected_Ds_donvi) {
        this.danhsach_tram()
      }
    },

    async danhsach_tram() {
      this.loading = true
      try {
        const res2 = await this.$root.context.get('/api/suyhao/VIEW_DS_DONVI', {
          prmDK: 2,
          prmDONVI: this.selected_Ds_donvi
        })

        const items_tram = (res2 && res2.data) || []
        this.service_Ds_tram = items_tram.map((x) => ({
          value: x.MA,
          text: x.TEN
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
        TEN_DV_BK: x.TEN_DV_BK,
        TO_KYTHUAT: x.TO_KYTHUAT || '',
        SYSTEMNAME: x.SYSTEMNAME || ''
      }))
    },

    async onSearch() {
      if (this.selected_Ds_tram) {
        this.loading = true
        this.items = []

        try {
          const res = await this.$root.context.get('/api/suyhao/VIEW_DS_DONVI', {
            prmDK: 3,
            prmDONVI: this.selected_Ds_tram
          })
          const rawItems = (res && res.data) || []
           this.fields = this.getDetailFields()
          this.items = this.mapItems(rawItems)
        } catch (e) {
          this.items = []
        } finally {
          this.loading = false
        }
      }
    }
  }
}
</script>

<style src="./index.css"></style>
