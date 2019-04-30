import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  initData() {
    return Api.get(API.INIT_DATA).then(res => res.data)
  },
  createProfile(params) {
    return Api.post(API.PROFILE, params).then(res => res.data)
  },
  updateProfile(params) {
    return Api.put(API.PROFILE, params).then(res => res.data)
  },
  getProfile(profileId) {
    return Api.get(API.PROFILE + '/' + profileId).then(res => res.data)
  },
  updateSettings(params) {
    return Api.put(API.PROFILE + '/settings', params).then(res => res.data)
  }
}
