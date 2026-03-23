import Vue from 'vue'
import Router from 'vue-router'
import BlankLayout from '@/layouts/BlankLayout'
import MainLayout from '@/layouts/MainLayout'
import Login from '@/modules/AUTH/Login'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '',
      component: MainLayout,
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/modules/index')
        },
        {
          path: 'demo',
          name: 'Demo',
          component: () => import('@/modules/Demo')
        },
        {
          // NOTE: Màn hình báo cáo demo, mở ở tab mới từ nút "Tạo báo cáo"
          path: 'demo-report',
          name: 'DemoReport',
          component: () => import('@/modules/Demo/Report')
        },
        {
          path: 'diemtin_tb_kll',
          name: 'Điểm tin thuê bao KLL',
          component: () => import('@/modules/Thuebao_KLL/DiemTinKLL')
        },
        {
          path: 'ob_tb_kll',
          name: 'OB thuê bao KLL',
          component: () => import('@/modules/Thuebao_KLL/OB_TBKLL')
        },
        {
          path: 'no_ob_tb_kll',
          name: 'Danh sách thuê bao không OB',
          component: () => import('@/modules/Thuebao_KLL/NO_OB_TBKLL')
        },
        {
          path: 'diemtin_suyhao',
          name: 'Điểm tin suy hao',
          component: () => import('@/modules/SuyHao/DiemTinSuyHao')
        },
        {
          path: 'diemtin_suyhao_thang',
          name: 'Điểm tin suy hao tổng hợp',
          component: () => import('@/modules/SuyHao/DiemTinSuyHao_Thang')
        },
        {
          path: 'diemtin_suyhao_tungay_denngay',
          name: 'Điểm tin suy hao từ ngày đến ngày',
          component: () => import('@/modules/SuyHao/DiemTinSuyHao_TUNGAY_DENNGAY')
        },
        {
          path: 'config_olt',
          name: 'Danh sách olt theo đơn vị',
          component: () => import('@/modules/SuyHao/CONFIG_OLT')
        }
      ]
    },
    {
      path: '/auth',
      component: BlankLayout,
      meta: { requiresAuth: false },
      children: [
        {
          path: 'login',
          name: 'Login',
          component: Login,
          meta: { requiresAuth: false }
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth !== false) {
    const token = localStorage.getItem('app-token')
    if (!token) {
      next({ name: 'Login' })
    } else {
      try {
        const tokenData = JSON.parse(token)
        if (!tokenData || !tokenData.token) {
          next({ name: 'Login' })
        } else {
          next()
        }
      } catch (e) {
        next({ name: 'Login' })
      }
    }
  } else {
    next()
  }
})

export default router
