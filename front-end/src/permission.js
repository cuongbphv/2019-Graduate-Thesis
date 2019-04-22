import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// permission judge function
function hasPermission(roles, permissionRoles) {
  console.log(roles, permissionRoles)
  if (roles.includes('admin')) return true // admin permission passed directly
  if (!permissionRoles) return true
  return roles.some(role => permissionRoles.indexOf(role) >= 0)
}

const whiteList = ['/login', '/auth-redirect', '/home'] // no redirect whitelist

router.beforeEach((to, from, next) => {
  // start progress bar
  NProgress.start()
  if (getToken()) { /* has token*/
    // if has logged in and go to /login page
    if (to.path === '/login') {
      // redirect to home page
      next({ path: '/' })
      // if current page is dashboard will not trigger	afterEach hook, so manually handle it
      NProgress.done()
    } else { // go to other page
      // check role of user who logged in and hasn't had role yet

      store.dispatch('GenerateRoutes', ['admin']).then(accessRoutes => { // every page
        console.log(accessRoutes)
        router.addRoutes(accessRoutes)
        // set the replace: true so the navigation will not leave a history record
        next({ ...to, replace: true })
      })

      if (store.getters.roles.length === 0) {
        store
          .dispatch('GetUserInfo')
          .then(res => {
            // note: roles must be a object array! such as: [{id: '1', name: 'editor'}, {id: '2', name: 'developer'}]
            const roles = res.data.roles
            store.dispatch('GenerateRoutes', { roles }).then(accessRoutes => {
              router.addRoutes(accessRoutes)
              // set the replace: true so the navigation will not leave a history record
              next({ ...to, replace: true })
            })
          })
          .catch(err => {
            store.dispatch('FedLogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else { // otherwise user had role after logged in
        // validate user has permission to access to page
        if (hasPermission(store.getters.roles, to.meta.roles)) {
          // if ok go to page
          next()
        } else {
          // else redirect to error 401 page unauthorized
          next({ path: '/401', replace: true, query: { noGoBack: true }})
        }
      }
    }
  } else { /* has no token*/
    // check page user access in white list can access without permission
    if (whiteList.indexOf(to.path) !== -1) {
      // ok go to page
      next()
    } else { // else go to login and keep url to redirect after login
      next(`/login?redirect=${to.path}`)
      // if current page is login will not trigger afterEach hook, so manually handle it
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
