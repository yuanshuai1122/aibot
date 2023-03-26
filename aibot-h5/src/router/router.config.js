/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  // {
  //   path: '/register',
  //   component: () => import('@/views/register'),
  //   hidden: true
  // },
  {
    path: '/',
    component: () => import('@/views/layouts/index'),
    redirect: '/home',
    meta: {
      title: '首页',
      keepAlive: false
    },
    children: [
      {
        path: '/index',
        name: 'Chat',
        component: () => import('@/views/home/chat'),
        meta: { title: '对话', keepAlive: false }
      },
      {
        path: '/edit',
        name: 'Classify',
        component: () => import('@/views/home/edit'),
        meta: { title: '创作', keepAlive: false }
      },
      {
        path: '/task',
        name: 'Task',
        component: () => import('@/views/home/task'),
        meta: { title: '任务', keepAlive: false }
      },
      {
        path: '/about',
        name: 'About',
        component: () => import('@/views/home/about'),
        meta: { title: '关于我', keepAlive: false }
      }
    ]
  }
]
