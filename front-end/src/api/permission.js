import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getListPermissions(params) {
    return Api.get(API.PERMISSION, { params: params }).then(res => res.data)
  },
  createNewPermission(params) {
    return Api.post(API.PERMISSION, params).then(res => res.data)
  },
  editPermissionById(params) {
    return Api.put(API.PERMISSION, params).then(res => res.data)
  },
  deletePermissions(params) {
    return Api.delete(API.PERMISSION, { params: params }).then(res => res.data)
  }
}
