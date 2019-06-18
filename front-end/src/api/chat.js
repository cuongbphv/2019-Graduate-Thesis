import Api from '@/api/api'
import { API } from '@/utils/constants'

export default {
  createNewConversation(param) {
    return Api.post(API.CHAT, param).then(res => res.data)
  },
  getAllConversationByUserId(userId, size) {
    return Api.get(API.CHAT, {
      params: {
        user_id: userId,
        size: size
      }
    }).then(res => res.data)
  },
  getConversationById(conversationId) {
    return Api.get(API.CHAT + `/${conversationId}`).then(res => res.data)
  },
  addMessage(message, conversationId) {
    return Api.post(API.CHAT + `/${conversationId}` + API.MESSAGE, message)
      .then(res => res.data)
  },
  getMessageHistory(conversationId, size) {
    return Api.get(API.CHAT + `/${conversationId}` + API.MESSAGE, {
      params: {
        size: size
      }
    }).then(res => res.data)
  },
  updateMessageStatus(message, conversationId) {
    return Api.put(API.CHAT + `/${conversationId}` + API.MESSAGE, message)
      .then(res => res.data)
  }
}
