import { Status } from '@/utils/constants'
import address from '@/api/address'

const getAddressByUserId = ({ commit }) => {
  return address.getAddressByUserId().then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_ADDRESS_BY_USERID', res.data)
    }
  }).catch(error => {
    throw error
  })
}
const getAddressByAddressId = ({ commit }, id) => {
  return address.getAddressByAddressId(id).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_ADDRESS_BY_ID', res.data)
    }
  }).catch(error => {
    throw error
  })
}
const createAddress = ({ commit }, params) => {
  return address.createAddress(params).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('CREATE_ADDRESS', res.data)
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}
const updateAddress = ({ commit }, params) => {
  return address.updateAddress(params, params.id).then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('UPDATE_ADDRESS', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}
const deleteAddressByIds = ({ commit }, ids) => {
  return address.deleteAddress(ids).then(res => {
    if (res.status === Status.SUCCESS) {
      console.info(res)
    }
  }).catch(error => {
    throw error
  })
}
export default {
  getAddressByUserId,
  getAddressByAddressId,
  createAddress,
  updateAddress,
  deleteAddressByIds
}
