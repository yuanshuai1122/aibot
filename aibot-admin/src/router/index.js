import Vue from 'vue'
import Router from 'vue-router'
import NProgress from "nprogress";
import { getRole, getToken } from '@/utils/auth'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import { getLoginRole } from '@/api/user'

// 路由
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },

  {
    path: '/user',
    component: Layout,
    meta: { title: '用户管理', icon: 'dashboard' },
    children: [
      {
      path: '/common',
      name: 'user',
      component: () => import('@/views/user/index'),
      meta: { title: '普通用户', icon: 'dashboard' }
      },
      {
        path: '/tenant',
        hidden: getRole() !== 'superAdmin',
        name: 'tenant',
        component: () => import('@/views/tenant/index'),
        meta: { title: '渠道商', icon: 'dashboard' }
      },

    ]
  },

  {
    path: '/order',
    component: Layout,
    children: [
      {
        path: '/order/index',
        name: 'order',
        component: () => import('@/views/order/index'),
        meta: { title: '订单管理', icon: 'form' }
      }
    ]
  },
  {
    path: '/product',
    component: Layout,
    children: [
      {
        path: '/product/index',
        name: 'product',
        component: () => import('@/views/product/index'),
        meta: { title: '产品管理', icon: 'form' }
      }
    ]
  },
  {
    path: '/distribution',
    component: Layout,
    children: [
      {
        path: '/distribution/index',
        name: 'distribution',
        component: () => import('@/views/distribution/index'),
        meta: { title: '分销管理', icon: 'form' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    children: [
      {
        path: '/system/index',
        hidden: getRole() !== 'superAdmin',
        name: 'system',
        component: () => import('@/views/system/index'),
        meta: { title: '系统管理', icon: 'form' }
      }
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes,
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}
const whiteList = ['/login']
console.log(getToken())
// 路由前置守卫
router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    if (to.path === '/login') {
      next({
        path: '/index'
      })
      NProgress.done()
    } else {
      next()
    }
  }else {
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login`) // 否则全部重定向到登录页
      // next() // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router
