import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  register(params) {
    return Api.post(API.REGISTER, params).then(res => res.data)
  },
  loginLocal(params) {
    return Api.post(API.LOGIN, params).then(res => res.data)
  },
  initData() {
    return Api.get(API.INIT_DATA).then(res => res.data)
  }
}
