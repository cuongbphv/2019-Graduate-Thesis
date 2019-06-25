import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  createRating(params) {
    return Api.post(API.RATING, params).then(res => res.data)
  },
  updateRating(params, id) {
    return Api.put(API.RATING + `/${id}`, params).then(res => res.data)
  },
  getRating(id) {
    return Api.get(API.RATING + `/${id}`).then(res => res.data)
  },
  getRatingForUser(userId, size) {
    return Api.get(API.RATING, {
      params: {
        user_id: userId,
        size: size
      }}).then(res => res.data)
  },
  deleteRating(id) {
    return Api.delete(API.RATING + `/${id}`).then(res => res.data)
  }
}
