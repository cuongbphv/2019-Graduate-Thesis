import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  importLocationData(params) {
    return Api.post(API.IMPORT_LOCATION, params).then(res => res.data)
  },
  getPagingLocation(params) {
    return Api.get(API.LOCATION, { params: params }).then(res => res.data)
  },
  getAllLocation(params) {
    return Api.get(API.GET_LIST_LOCATION, { params: params }).then(res => res.data)
  },
  addNewProvince(params) {
    return Api.post(API.PROVINCE, params).then(res => res.data)
  },
  updateProvince(id, params) {
    return Api.put(API.PROVINCE + '/' + id, params).then(res => res.data)
  },
  deleteProvinces(params) {
    return Api.delete(API.PROVINCE, {
      params: {
        ids: Array.isArray(params) && params.join(',') || params
      }
    }).then(res => res.data)
  },
  addNewDistrict(params) {
    return Api.post(API.DISTRICT, params).then(res => res.data)
  },
  updateDistrict(id, params) {
    return Api.put(API.DISTRICT + '/' + id, params).then(res => res.data)
  },
  deleteDistricts(params) {
    return Api.delete(API.DISTRICT, {
      params: {
        province_id: params.provinceId,
        district_ids: Array.isArray(params.ids) && params.ids.join(',') || params.ids
      }
    }).then(res => res.data)
  },
  addNewWard(params) {
    return Api.post(API.WARD, params).then(res => res.data)
  },
  updateWard(id, params) {
    return Api.put(API.WARD + '/' + id, params).then(res => res.data)
  },
  deleteWards(params) {
    return Api.delete(API.WARD, {
      params: {
        province_id: params.provinceId,
        district_id: params.districtId,
        ward_ids: Array.isArray(params.ids) && params.ids.join(',') || params.ids
      }
    }).then(res => res.data)
  }
}
