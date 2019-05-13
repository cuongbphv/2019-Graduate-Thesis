import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getAddressByUserId() {
    return Api.get(API.ADDRESS).then(res => res.data)
  },
  getAddressByAddressId(id) {
    return Api.get(API.ADDRESS + `/${id}`).then(res => res.data)
  },
  createAddress(params) {
    return Api.post(API.ADDRESS, params).then(res => res.data)
  },
  updateAddress(params, id) {
    return Api.put(API.ADDRESS + `/${id}`, params).then(res => res.data)
  },
  deleteAddress(params) {
    return Api.delete(API.ADDRESS, {
      params: {
        ids: Array.isArray(params) && params.join(',') || params
      }
    }).then(res => res.data)
  }
}
