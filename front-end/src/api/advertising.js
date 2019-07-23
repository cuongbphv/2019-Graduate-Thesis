import Api from '@/api/api'
import { API } from '@/utils/constants'

export default {
  createNewAdvertising(params) {
    return Api.post(API.ADVERTISING, params).then(res => res.data)
  },
  updateAdvertising(id, body) {
    return Api.put(API.ADVERTISING + '/' + id, body).then(res => res.data)
  },
  getAdvertisingDetail(id) {
    return Api.get(API.ADVERTISING + '/' + id).then(res => res.data)
  },
  getNewClassifiedAds(params) {
    return Api.get(API.NEW_ADVERTISING, { params: params }).then(res => res.data)
  },
  getUserHistoryClassifiedAds(params, userId) {
    return Api.get(API.USER_HISTORY_ADS + `/${userId}`, { params: params }).then(res => res.data)
  },
  changeStatusAds(id, status) {
    return Api.put(API.ADVERTISING + '/' + id + API.STATUS + '?status=' + status).then(res => res.data)
  },
  uploadTempImage(params) {
    return Api.post(API.UPLOAD_TEMP_IMAGE, params).then(res => res.data)
  },
  removeTempImage(params) {
    return Api.delete(API.REMOVE_TEMP_IMAGE, { params: params }).then(res => res.data)
  },
  fullTextSearch(params) {
    return Api.get(API.FULL_TEXT_SEARCH, { params: params }).then(res => res.data)
  },
  getTopCategoryPost(params) {
    return Api.get(API.TOP_CATEGORY_POST, { params: params }).then(res => res.data)
  },
  changeTradingStatusAds(id, status) {
    return Api.put(API.ADVERTISING + `/${id}/trading_status`, status).then(res => res.data)
  },
  getSaveAds(params, id) {
    return Api.get(API.ADVERTISING + API.SAVES + `/${id}`, { params: params }).then(res => res.data)
  },
  saveAds(id) {
    return Api.put(API.ADVERTISING + `/${id}` + API.SAVES).then(res => res.data)
  },
  removeSaveAds(id) {
    return Api.delete(API.ADVERTISING + `/${id}` + API.SAVES).then(res => res.data)
  }
}
