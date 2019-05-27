import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getListRole() {
    return Api.get(API.ROLE).then(res => res.data)
  },
  createNewRole(params) {
    return Api.post(API.ROLE, params).then(res => res.data)
  },
  updateRole(params) {
    return Api.put(API.ROLE, params).then(res => res.data)
  },
  deleteRole(params) {
    return Api.delete(API.ROLE, { params: params }).then(res => res.data)
  }
}
