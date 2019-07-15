import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  changePassword(params, id) {
    return Api.put(API.USER + `/${id}` + API.PASSWORD, params).then(res => res.data)
  },
  banUser(id) {
    return Api.put(API.USER + `/${id}` + API.BAN).then(res => res.data)
  },
  unLockUser(id) {
    return Api.put(API.USER + `/${id}` + API.UNLOCK).then(res => res.data)
  },
  deleteUser(id) {
    return Api.delete(API.USER + `/${id}`).then(res => res.data)
  }
}
