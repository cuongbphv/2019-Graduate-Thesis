import profile from '@/api/profile'
import { Status } from '@/utils/constants'

const initData = ({ commit }) => {
  return profile.initData().then(res => {
    if (res.status === Status.SUCCESS) {
      if (res.data.authorities) {
        res.data.authorities = res.data.authorities.map(permission => permission.authority)
      }
      commit('INIT_DATA', res.data)
      return res.data.authorities
    }
  })
}

const createProfile = ({ commit }, params) => {
  return profile.createProfile(params).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('CREATE_PROFILE', params)
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const updateProfile = ({ commit }, params) => {
  return profile.updateProfile(params).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('UPDATE_PROFILE', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getProfile = ({ commit }, profileId) => {
  return profile.getProfile(profileId).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PROFILE', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

export default {
  initData,
  createProfile,
  updateProfile,
  getProfile
}
