<template src="./index.template.html"></template>

<script>
import KLLNoObForm from '../KLLNoObForm.vue'

export default {
  name: 'NO_OB_TBKLL',
  data: () => ({
    loadingSearch: false,
    loadingList: false,

    maTb: '',
    searchText: '',

    searchItems: [],
    listItems: [],

    selectedRecord: null,
    showNoObModal: false,

    searchFields: [
      { key: 'index', label: '#', thClass: 'text-center', tdClass: 'text-center' },
      { key: 'center', label: 'Trung tâm viễn thông' },
      { key: 'area', label: 'Địa bàn' },
      { key: 'maTb', label: 'Mã tb' },
      { key: 'subscriberName', label: 'Tên thuê bao' },
      { key: 'installAddress', label: 'Địa chỉ lắp đặt' },
      { key: 'status', label: 'Trạng thái' },
      { key: 'note', label: ''}
    ],

    listFields: [
      { key: 'index', label: '#', thClass: 'text-center', tdClass: 'text-center' },
      { key: 'center', label: 'Trung tâm viễn thông' },
      { key: 'area', label: 'Địa bàn' },
      { key: 'maTb', label: 'Mã tb' },
      { key: 'subscriberName', label: 'Tên thuê bao' },
      { key: 'installAddress', label: 'Địa chỉ lắp đặt' },
      { key: 'requester', label: 'Người đề nghị' },
      { key: 'reason', label: 'Lý do' }
    ]
  }),
  created() {
    this.fetchList()
  },
  methods: {
    mapSearchItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        center: x.C2 || x.TRUNGTAM || '',
        area: x.C4 || x.DIABAN || '',
        maTb: x.MA_TB || x.FIBER || '',
        subscriberName: x.TEN_TB || '',
        installAddress: x.DIACHI_LD || x.DIACHI_TB || '',
        status: x.TRANGTHAI_TB || x.TRANG_THAI || ''
      }))
    },

    mapListItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        center: x.TRUNGTAM || x.C2 || '',
        area: x.DIABAN || x.C4 || '',
        maTb: x.MA_TB || '',
        subscriberName: x.TEN_TB || '',
        installAddress: x.DIACHI_LD || x.DIACHI_TB || '',
        requester: x.NGUOI_DE_NGHI || x.NGUOI_DENGHI || '',
        reason: x.NGUYENNHAN || x.LYDO || ''
      }))
    },

    openNoObModal(item) {
      console.log(item);
      this.selectedRecord = item
      this.showNoObModal = true
    },

    async onSearchByMaTb() {
      this.loadingSearch = true
      this.searchItems = []
      try {
        const res = await this.$root.context.get('/api/thuebao_kll/TIMKIEM_TB_KHONG_OB_VIEW', {
          prmTHONGTIN: this.maTb
        })
        const items = (res && res.data) || []
        this.searchItems = this.mapSearchItems(items)
      } catch (e) {
        this.searchItems = []
      } finally {
        this.loadingSearch = false
      }
    },

    async fetchList() {
      this.loadingList = true
      this.listItems = []
      try {
        const res = await this.$root.context.get('/api/thuebao_kll/DANHSACH_TB_KHONG_OB_VIEW', {})
        const items = (res && res.data) || []
        this.listItems = this.mapListItems(items)
      } catch (e) {
        this.listItems = []
      } finally {
        this.loadingList = false
      }
    }
  }
}
</script>

<style src="./index.css"></style>

