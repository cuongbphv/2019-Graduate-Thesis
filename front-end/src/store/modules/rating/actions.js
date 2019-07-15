import rating from '@/api/rating'
import { Status } from '@/utils/constants'

const createRating = ({ commit }, param) => {
  return rating.createRating(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('CREATE_RATING', res.data)
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const getRating = ({ commit }, id) => {
  return rating.getRating(id).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_RATING', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getAllRating = ({ commit }, id) => {
  return rating.getAllRating(id).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_ALL_RATING', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const updateRating = ({ commit }, params) => {
  return rating.updateRating(params, params.id).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('UPDATE_RATING', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getRatingForUser = ({ commit }, params) => {
  return rating.getRatingForUser(params.userId, params.size).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_RATING_FOR_USER', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const deleteRating = ({ commit }, id) => {
  return rating.deleteRating(id).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('DELETE_RATING', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

export default {
  createRating,
  getRating,
  updateRating,
  getRatingForUser,
  deleteRating,
  getAllRating
}
