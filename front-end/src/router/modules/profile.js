const profileRouter = {
  path: '/profile/:id',
  redirect: '/profile/:id/history',
  hidden: true,
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
    },
    {
      path: 'address',
      name: 'AddressManagement',
      component: () => import('@/views/profile/profile-detail/Address'),
      meta: {
        permission: 'MANAGE_ACCOUNT_ADDRESS'
      }
    },
    {
      path: 'rating',
      name: 'Rating',
      component: () => import('@/views/profile/profile-detail/Rating'),
      meta: {
        permission: 'MANAGE_ACCOUNT_ADDRESS'
      }
    },
    {
      path: 'saved',
      name: 'Saved',
      component: () => import('@/views/profile/profile-detail/Saved'),
      meta: {
        permission: 'MANAGE_ACCOUNT_ADDRESS'
      }
    }
  ]
}

export default profileRouter
