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
