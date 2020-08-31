import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/entrance/index'),
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/entrance/loginForm'),
        hidden: true
      }
    ]
  },
  {
    path: '/register',
    component: () => import('@/views/entrance/index'),
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/entrance/registerForm'),
        hidden: true
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/landManage',
    children: [{
      path: 'landManage',
      components: {
        default: () => import('@/views/landManage/index'),
        content:  () => import('@/views/mainMap/index'),
        tools:  () => import('@/views/mapTool/index'),
      }
    }]
  },
  {
    path: '/growManage',
    component: Layout,
    children: [
      {
        path: '',
        components: {
          default: () => import('@/views/growManage/index'),
          content:  () => import('@/views/mainMap/index'),
          tools:  () => import('@/views/mapTool/index'),
        }
      }
    ]
  },
  {
    path: '/vegetationManage',
    component: Layout,
    children: [
      {
        path: '',
        components: {
          default: () => import('@/views/vegetationManage/index'),
          content:  () => import('@/views/mainMap/index'),
          tools:  () => import('@/views/mapTool/index'),
        }
      }
    ]
  },
  {
    path: '/meteorologicalService',
    component: Layout,
    children: [
      {
        path: '',
        components: {
          default: () => import('@/views/meteorologicalService/index'),
          content:  () => import('@/views/mainMap/index'),
          tools:  () => import('@/views/mapTool/index'),
        }
      }
    ]
  },
  // 用户管理;
  {
    path: '/userManage',
    component: Layout,
    redirect: '/userManage/detail',
    children: [
      {
        path: 'detail',
        name: 'userDetail',
        components: {
          default: () => import('@/views/userMange/index'),
          content:  () => import('@/views/userMange/userDetail'),
        }
      },
      {
        path: 'settings',
        name: 'userSettings',
        components: {
          default: () => import('@/views/userMange/index'),
          content:  () => import('@/views/userMange/userSettings'),
        },
        redirect: '/userManage/settings/accountSecurity',
        children: [
          {
            path: 'accountSecurity',
            name: 'accountSecurity',
            component: () => import('@/views/userMange/accountSecurity')
          },
          {
            path: 'accountManage',
            name: 'accountManage',
            component: () => import('@/views/userMange/accountManage')
          },
        ]
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
