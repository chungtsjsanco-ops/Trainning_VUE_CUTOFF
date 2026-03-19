<template src="./index.template.html"></template>

<script>
import KLLNoObForm from '../KLLNoObForm.vue'

export default {
  name: 'NO_OB_TBKLL',
  components: {
    KLLNoObForm
  },
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
      { key: 'fiber', label: 'Mã tb' },
      { key: 'subscriberName', label: 'Tên thuê bao' },
      { key: 'installAddress', label: 'Địa chỉ lắp đặt' },
      { key: 'status', label: 'Trạng thái' },
      { key: 'note', label: '' }
    ],

    listFields: [
      { key: 'index', label: '#', thClass: 'text-center', tdClass: 'text-center' },
      { key: 'center', label: 'Trung tâm viễn thông' },
      { key: 'area', label: 'Địa bàn' },
      { key: 'fiber', label: 'Mã tb' },
      { key: 'subscriberName', label: 'Tên thuê bao' },
      { key: 'installAddress', label: 'Địa chỉ lắp đặt' },
      { key: 'requester', label: 'Người đề nghị' },
      { key: 'reason', label: 'Lý do' },
      { key: 'note', label: '' }
    ]
  }),
  created() {
    this.fetchList()
  },
  methods: {
    handleSaved() {
      this.showNoObModal = false // đóng modal

      this.onSearchByMaTb('') // reload theo mã TB
      this.fetchList() // reload list dưới
    },
    mapSearchItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        center: x.C2 || x.TRUNGTAM || '',
        area: x.C4 || x.DIABAN || '',
        fiber: x.MA_TB || x.FIBER || '',
        subscriberName: x.TEN_TB || '',
        installAddress: x.DIACHI_LD || x.DIACHI_TB || '',
        status: x.TRANGTHAI_TB || x.TRANG_THAI || '',
        thuebaoId: x.THUEBAO_ID || x.ID || null
      }))
    },

    mapListItems(apiItems) {
      if (!Array.isArray(apiItems)) return []
      return apiItems.map((x) => ({
        thuebaoId: x.THUEBAO_ID || x.ID || null,
        center: x.TRUNGTAM || x.C2 || '',
        area: x.DIABAN || x.C4 || '',
        fiber: x.MA_TB || x.FIBER || '',
        subscriberName: x.TEN_TB || '',
        installAddress: x.DIACHI_LD || x.DIACHI_TB || '',
        requester: x.NGUOI_DE_NGHI || x.NGUOI_DENGHI || '',
        reason: x.NGUYENNHAN || x.LYDO || '',
        note: x.GHI_CHU || ''
      }))
    },

    openNoObModal(item) {
      this.selectedRecord = item
      this.showNoObModal = true
    },

    async onSearchByMaTb(maTB) {
      this.loadingSearch = true
      this.searchItems = []
      try {
        const res = await this.$root.context.get('/api/thuebao_kll/TIMKIEM_TB_KHONG_OB_VIEW', {
          prmTHONGTIN: maTB
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
    },

    confirmDelete(data) {
      this.$bvModal
        .msgBoxConfirm(`Bạn có chắc muốn xóa thuê bao ${data.item.subscriberName} ${data.item.fiber}?`, {
          title: 'Xác nhận',
          size: 'sm',
          okVariant: 'danger',
          okTitle: 'Xóa',
          cancelTitle: 'Hủy',
          centered: true
        })
        .then((value) => {
          if (value) {
            this.deleteTb(data.item.thuebaoId)
          }
        })
    },

    async deleteTb(thuebaoId) {
       try {
        const res = await this.$root.context.post(
          '/api/thuebao_kll/DANHSACH_TB_KHONG_OB_DEL',
          {
            prmTHUEBAO_ID : thuebaoId,
            prmNGUOI_CAPNHAT : 'admin'
          }
        )

        // Backend quy ước error = "200" là OK, còn lại là lỗi nghiệp vụ
        if (res && (res.error === '200' || res.error === 200)) {
          this.$bvToast &&
            this.$bvToast.toast('Xóa thành công thuê bao KLL không cần OB.', {
              title: 'Thành công',
              variant: 'success',
              autoHideDelay: 2500,
              toaster: 'b-toaster-bottom-right'
            })
          this.fetchList()
        } 
        else {
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
      } 
      catch (e) {
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

<style src="./index.css"></style>
