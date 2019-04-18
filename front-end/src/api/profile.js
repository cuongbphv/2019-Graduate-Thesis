import Api from '../api/api'
import { API } from '../utils/constants'

export default {

  createProfile(params) {
    return Api.post(API.PROFILE, params).then(res => res)
  },
  updateProfile(params) {
    return Api.put(API.PROFILE, params).then(res => res)
  },
  getProfile(profileId) {
    return Api.get(API.PROFILE + '/' + profileId).then(res => res)
  }
}
