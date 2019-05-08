import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getCategoryPaging(params) {
    return Api.get(API.CATEGORY, { params: params }).then(res => res.data)
  },
  getCategoryById(id) {
    return Api.get(API.CATEGORY + `/${id}`).then(res => res.data)
  },
  createCategory(params) {
    return Api.post(API.CATEGORY, params).then(res => res.data)
  },
  updateCategory(params, id) {
    return Api.put(API.CATEGORY + `/${id}`, params).then(res => res.data)
  },
  updateMetadata(params, id) {
    return Api.put(API.CATEGORY + `/${id}` + API.METADATA, params).then(res => res.data)
  },
  updateFilter(params, id) {
    return Api.put(API.CATEGORY + `/${id}` + API.FILTER, params).then(res => res.data)
  },
  getMetadata(id) {
    return Api.get(API.CATEGORY + `/${id}` + API.METADATA).then(res => res.data)
  },
  getFilter(id) {
    return Api.get(API.CATEGORY + `/${id}` + API.FILTER).then(res => res.data)
  }
}
