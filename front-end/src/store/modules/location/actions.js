import location from '../../../api/location'
import { Status } from '@/utils/constants'

const importLocationData = ({ commit }, fileData) => {
  return location.importLocationData(fileData).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('IMPORT_LOCATION_DATA', res.data)
    }
  }).catch(error => {
    throw error
  })
}

export default {
  importLocationData
}
