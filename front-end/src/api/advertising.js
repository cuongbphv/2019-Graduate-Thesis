import Api from '@/api/api'
import { API } from '@/utils/constants'

export default {
  createNewAdvertising(params) {
    return Api.post(API.ADVERTISING, params).then(res => res.data)
  },
  getAdvertisingDetail(id) {
    return Api.get(API.ADVERTISING + '/' + id).then(res => res.data)
  },
  getNewClassifiedAds(params) {
    return Api.get(API.NEW_ADVERTISING, { params: params }).then(res => res.data)
  },
  uploadTempImage(params) {
    return Api.post(API.UPLOAD_TEMP_IMAGE, params).then(res => res.data)
  },
  removeTempImage(params) {
    return Api.delete(API.REMOVE_TEMP_IMAGE, { params: params }).then(res => res.data)
  },
  fullTextSearch(params) {
    return Api.get(API.FULL_TEXT_SEARCH, { params: params }).then(res => res.data)
  }
}
