import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteListNoToken = ['/login', '/auth-redirect', '/home'] // no redirect whitelist
const whiteListToken = ['/login', '/auth-redirect', '/home', '/404', '/401']

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
    } else if (whiteListToken.indexOf(to.path) !== -1) {
      next() // ok go to page
    } else { // go to other page
      // check role of user who logged in and hasn't had role yet
      const permissions = store.getters['profile/authorities'] // get all permission of logged in
      // check list permission of who logged in with permission of router to
      if (permissions.includes(to.meta.permission)) { // has permission
        // next({ ...to, replace: true }) // next to route
        next()
      } else { // otherwise user has no permission
        next({ path: '/401', replace: true, query: { noGoBack: true }}) // return page unauthorized
      }
    }
  } else { /* has no token*/
    // check page user access in white list can access without permission
    if (whiteListNoToken.indexOf(to.path) !== -1) {
      next() // ok go to page
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
