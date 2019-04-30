import location from '@/api/location'
import { Status } from '@/utils/constants'
import CommonModelMap from '../../models/CommonModelMap'

const importLocationData = ({ commit }, fileData) => {
  return location.importLocationData(fileData).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('IMPORT_LOCATION_DATA', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const loadListPagingLocation = ({ commit }, query) => {
  const param = CommonModelMap.toParamGetList(query)
  return location.getPagingLocation(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAGING_LOCATION', res.data)
    }
  }).catch(error => {
    throw error
  })
}

export default {
  importLocationData,
  loadListPagingLocation
}
