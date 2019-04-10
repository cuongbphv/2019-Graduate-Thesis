const profileRouter = {
  path: '/profile',
  redirect: '/profile/:id',
  name: 'Profile',
  component: () => import('@/views/profile/Profile'),
  children: [
    {
      path: ':id',
      redirect: '/profile/:id/history',
      name: 'ProfileDetail',
      component: () => import('@/views/profile/ProfileDetail'),
      meta: { title: 'profileDetail' },
      children: [
        {
          path: 'history',
          name: 'ProfileHistory',
          component: () => import('@/views/profile/profile-detail/History')
        }
      ]
    }
  ]
}

export default profileRouter
