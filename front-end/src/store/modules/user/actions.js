import user from '@/api/user'
import { Status } from '@/utils/constants'

const changePassword = ({ commit }, param) => {
  return user.changePassword(param, param.id).then(res => {
    if (res.status === Status.SUCCESS) {
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const banUser = ({ commit }, id) => {
  return user.banUser(id).then(res => {
    if (res.status === Status.SUCCESS) {
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const unlockUser = ({ commit }, id) => {
  return user.unLockUser(id).then(res => {
    if (res.status === Status.SUCCESS) {
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const deleteUser = ({ commit }, id) => {
  return user.deleteUser(id).then(res => {
    if (res.status === Status.SUCCESS) {
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

export default {
  changePassword,
  banUser,
  unlockUser,
  deleteUser
}
