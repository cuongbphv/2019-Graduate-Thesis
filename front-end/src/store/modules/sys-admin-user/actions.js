import sysAdmin from '@/api/sys-admin'
import { Status } from '@/utils/constants'
import CommonModelMap from '../../models/CommonModelMap'

const getListUserPaging = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return sysAdmin.getListUserPaging(param).then(res => {
    if (res.status === Status.SUCCESS) {
      if (res.data) {
        commit('SYS_ADMIN_GET_LIST_USER', res.data)
      }
    }
  }).catch(error => {
    throw error
  })
}
export default {
  getListUserPaging
}
