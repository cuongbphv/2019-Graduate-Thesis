import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  register(params) {
    return Api.post(API.REGISTER, params).then(res => res.data)
  },
  loginLocal(params) {
    return Api.post(API.LOGIN, params).then(res => res.data)
  },
  changePassword(params) {
    return Api.put(API.CHANGE_PASSWORD, params).then(res => res.data)
  },
  sendOTP(phone) {
    return Api.get(API.OTP, { params: phone }).then(res => res.data)
  },
  submitOTP(params) {
    return Api.post(API.OTP, params).then(res => res.data)
  }
}
