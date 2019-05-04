import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  importLocationData(params) {
    return Api.post(API.IMPORT_LOCATION, params).then(res => res.data)
  },
  getPagingLocation(params) {
    return Api.get(API.LOCATION, { params: params }).then(res => res.data)
  },
  addNewProvince(params) {
    return Api.post(API.PROVINCE, params).then(res => res.data)
  },
  addNewDistrict(params) {
    return Api.post(API.DISTRICT, params).then(res => res.data)
  },
  addNewWard(params) {
    return Api.post(API.WARD, params).then(res => res.data)
  },
  deleteProvinces(params) {
    return Api.delete(API.PROVINCE, {
      params: {
        ids: Array.isArray(params) && params.join(',') || params
      }
    }).then(res => res.data)
  },
  deleteDistricts(params) {
    return Api.delete(API.DISTRICT, {
      params: {
        province_ids: params.provinceId,
        ids: params.ids.join(',')
      }
    }).then(res => res.data)
  },
  deleteWards(params) {
    return Api.delete(API.WARD, { params: params }).then(res => res.data)
  }
}
