const profileRouter = {
  path: '/profile',
  redirect: '/profile/:id',
  name: 'Profile',
  component: () => import('@/views/profile/Profile'),
  children: [
    {
      path: ':id',
      component: () => import('@/views/profile/ProfileDetail'),
      name: 'ProfileDetail',
      meta: { title: 'profileDetail' }
    }
  ]
}

export default profileRouter
