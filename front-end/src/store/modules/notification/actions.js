import notification from '@/api/notification'
import { Status } from '@/utils/constants'

const getNotifications = ({ commit }, params) => {
  return notification.getNotificationsByUserId(params.userId, params.size).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_NOTIFICATIONS', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

export default {
  getNotifications
}
