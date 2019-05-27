import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'
import HomeLayout from '@/views/layout/HomeLayout'

/* Router Modules */
import profileRouter from './modules/profile'
import advertisingRouter from './modules/advertising'
import managementRouter from './modules/management'

export const allRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '/sys_admin_role',
    component: Layout,
    redirect: 'role',
    meta: {
      title: 'permission',
      icon: 'lock'
    },
    children: [
      {
        path: '/role',
        component: () => import('@/views/sys_admin/Role'),
        name: 'Role',
        meta: {
          title: 'role',
          permission: 'MANAGE_APP_ROLE'
        }
      },
      {
        path: '/permission',
        component: () => import('@/views/sys_admin/Permission'),
        name: 'Permission',
        meta: {
          title: 'access',
          permission: 'MANAGE_APP_PERMISSION'
        }
      }
    ]
  },
  {
    path: '/sys_admin_user',
    component: Layout,
    redirect: 'user_role',
    children: [
      {
        path: '/user_role',
        component: () => import('@/views/sys_admin/UserRole'),
        name: 'UserRole',
        meta: {
          title: 'user',
          icon: 'peoples',
          permission: 'MANAGE_USER_ROLE'
        }
      }
    ]
  },
  {
    path: '/admin',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: '/dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: {
          title: 'dashboard',
          icon: 'dashboard',
          noCache: true,
          affix: true,
          permission: 'VIEW_DASHBOARD'
        }
      }
    ]
  },
  {
    path: '',
    component: HomeLayout,
    redirect: 'home',
    children: [
      {
        path: '/home',
        component: () => import('@/views/home/Home'),
        name: 'Home',
        meta: {
          title: 'home',
          permission: 'VIEW_HOME'
        }
      },
      {
        path: '/search',
        component: () => import('@/views/search/SearchPage'),
        name: 'SearchPage',
        meta: {
          title: 'search-page',
          skip: true
        }
      },
      profileRouter,
      advertisingRouter
    ]
  },
  managementRouter,
  {
    path: '/icon',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/svg-icons/index'),
        name: 'Icons',
        meta: { title: 'icons', icon: 'icon', noCache: true }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  mode: 'history', // require service support
  routes: allRoutes
})
