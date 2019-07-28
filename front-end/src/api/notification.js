import Api from '@/api/api'
import { API } from '@/utils/constants'

export default {
  getNotificationsByUserId(userId, size) {
    return Api.get(API.NOTIFICATION, {
      params: {
        user_id: userId,
        size: size
      }
    }).then(res => res.data)
  }
}
