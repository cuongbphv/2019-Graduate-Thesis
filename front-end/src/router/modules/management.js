import Layout from '@/views/layout/Layout'

const managementRouter = {
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
    }
  ]
}

export default managementRouter
