import chat from '@/api/chat'
import { Status } from '@/utils/constants'

const createNewConversation = ({ commit }, param) => {
  return chat.createNewConversation(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('CREATE_CONVERSATION', res.data)
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const getAllConversationByUserId = ({ commit }, params) => {
  return chat.getAllConversationByUserId(params.userId, params.size).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_ALL_CONVERSATION', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getConversationById = ({ commit }, conversationId) => {
  return chat.getConversationById(conversationId).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_CONVERSATION_BY_ID', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const addMessage = ({ commit }, params) => {
  return chat.addMessage(params, params.conversationId).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('ADD_MESSAGE', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getMessageHistory = ({ commit }, params) => {
  return chat.getMessageHistory(params.id, params.size).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('MESSAGE_HISTORY', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const updateMessageStatus = ({ commit }, params) => {
  return chat.updateMessageStatus(params.message, params.conversationId).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('UPDATE_MESSAGE_STATUS', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

export default {
  createNewConversation,
  getAllConversationByUserId,
  getConversationById,
  addMessage,
  getMessageHistory,
  updateMessageStatus
}
