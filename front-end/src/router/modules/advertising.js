const advertisingRouter = {
  path: '/advertising',
  redirect: '/advertising/create',
  hidden: true,
  component: () => import('@/views/advertising/Advertising.vue'),
  name: 'ClassifiedAdvertising',
  children: [
    {
      path: 'create',
      name: 'CreateAdvertising',
      component: () => import('@/views/advertising/CreateAdvertising'),
      meta: {
        permission: 'CREATE_CLASSIFIED_ADVERTISING'
      }
    },
    {
      path: ':id',
      name: 'ClassifiedAdsDetail',
      component: () => import('@/views/advertising/ClassifiedAdsDetail'),
      meta: {
        skip: true
      }
    }
  ]
}

export default advertisingRouter
