import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getListUserPaging(params) {
    return Api.get(API.SYS_ADMIN_LIST_USER, { params: params }).then(res => res.data)
  }
}
