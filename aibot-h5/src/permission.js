import router from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import Cookies from 'js-cookie'
import {
  getToken
} from '@/utils/auth'

NProgress.configure({
  showSpinner: false
})

const whiteList = ['/login','/register']
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
