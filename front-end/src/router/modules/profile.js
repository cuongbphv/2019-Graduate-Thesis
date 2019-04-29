const profileRouter = {
  path: '/profile',
  redirect: '/profile/:id',
  name: 'Profile',
  hidden: true,
  component: () => import('@/views/profile/Profile'),
  children: [
    {
      path: ':id',
      redirect: '/profile/:id/history',
      name: 'ProfileDetail',
      component: () => import('@/views/profile/ProfileDetail'),
      meta: {
        title: 'profileDetail'
      },
      children: [
        {
          path: 'history',
          name: 'ProfileHistory',
          component: () => import('@/views/profile/profile-detail/History'),
          meta: {
            permission: 'VIEW_PROFILE_HISTORY'
          }
        },
        {
          path: 'info',
          name: 'ProfileInformation',
          component: () => import('@/views/profile/profile-detail/Information'),
          meta: {
            permission: 'MANAGE_PROFILE_INFORMATION'
          }
        },
        {
          path: 'settings',
          name: 'AccountSetting',
          component: () => import('@/views/profile/profile-detail/Setting'),
          meta: {
            permission: 'MANAGE_PROFILE_INFORMATION'
          }
        }
      ]
    }
  ]
}

export default profileRouter
