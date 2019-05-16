import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'
import HomeLayout from '@/views/layout/HomeLayout'

/* Router Modules */
import profileRouter from './modules/profile'
import advertisingRouter from './modules/advertising'
// import chartsRouter from './modules/charts'

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
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/index',
    meta: {
      title: 'permission',
      icon: 'lock',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/permission/page'),
        name: 'PagePermission',
        meta: {
          title: 'pagePermission'
        }
      },
      {
        path: 'directive',
        component: () => import('@/views/permission/directive'),
        name: 'DirectivePermission',
        meta: {
          title: 'directivePermission'
        }
      },
      {
        path: 'role',
        component: () => import('@/views/permission/role'),
        name: 'RolePermission',
        meta: {
          title: 'rolePermission'
        }
      }
    ]
  },
  {
    path: '/manage',
    component: Layout,
    meta: {
      title: 'management',
      icon: 'component'
    },
    children: [
      {
        path: 'location',
        component: () => import('@/views/management/location/Location'),
        name: 'Location',
        meta: {
          title: 'location',
          icon: 'location',
          permission: 'MANAGE_LOCATION'
        }
      },
      {
        path: 'category',
        component: () => import('@/views/management/category/Category'),
        name: 'Category',
        meta: {
          title: 'category',
          icon: 'list-category',
          permission: 'MANAGE_CATEGORY'
        }
      },
      {
        path: 'user',
        component: () => import('@/views/tab/index'),
        name: 'User Management',
        meta: { title: 'user', icon: 'peoples' }
      },
      {
        path: 'store',
        component: () => import('@/views/tab/index'),
        name: 'Store',
        meta: { title: 'store', icon: 'store' }
      }
    ]
  },

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
