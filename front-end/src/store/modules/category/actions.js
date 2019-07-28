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
const getListCategory = ({ commit }, query) => {
  const param = CommonModelMap.toParam(query)
  return category.getListCategory(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_LIST_CATEGORY', res.data)
    }
  }).catch(error => {
    throw error
  })
}
const getMetadataByCategoryId = ({ commit }, id) => {
  return category.getMetadata(id).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_METADATA', res.data)
    }
  }).catch(error => {
    throw error
  })
}
const getMetadatasByCategoryId = ({ commit }, id) => {
  return category.getMetadatas(id).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_METADATAS', res.data)
    }
  }).catch(error => {
    throw error
  })
}
const deleteCategoryByIds = ({ commit }, ids) => {
  return category.deleteCategory(ids).then(res => {
    if (res.status === Status.SUCCESS) {
      console.info(res)
    }
  }).catch(error => {
    throw error
  })
}
export default {
  loadListPagingCategory,
  getListCategory,
  getMetadataByCategoryId,
  deleteCategoryByIds,
  getMetadatasByCategoryId
}
