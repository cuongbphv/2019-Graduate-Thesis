import CommonModelMap from '../../models/CommonModelMap'
import { Status } from '@/utils/constants'
import category from '@/api/category'

const loadListPagingCategory = ({ commit }, query) => {
  const param = CommonModelMap.toParam(query)
  return category.getCategoryPaging(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAGING_CATEGORY', res.data)
    }
  }).catch(error => {
    throw error
  })
}

export default {
  loadListPagingCategory
}
