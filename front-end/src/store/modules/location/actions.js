import location from '@/api/location'
import { Status } from '@/utils/constants'
import CommonModelMap from '../../models/CommonModelMap'
import Location from '../../models/Location'

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
  const param = CommonModelMap.toParam(query)
  return location.getPagingLocation(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAGING_LOCATION', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const loadListLocation = ({ commit }, query) => {
  const param = CommonModelMap.toParam(query)
  return location.getAllLocation(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_LIST_LOCATION', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const addNewProvince = ({ commit }, province) => {
  const param = Location.toProvinceParam(province)
  return location.addNewProvince(param).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const addNewDistrict = ({ commit }, province) => {
  const param = Location.toListDistrictParam(province)
  return location.addNewDistrict(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('ADD_NEW_DISTRICT', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const addNewWard = ({ commit }, province) => {
  const param = Location.toListWardParam(province)
  return location.addNewWard(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('ADD_NEW_WARD', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const deleteProvinces = ({ commit }, ids) => {
  return location.deleteProvinces(ids).then(res => {
    if (res.status === Status.SUCCESS) {
      console.info(res)
    }
  }).catch(error => {
    throw error
  })
}

const deleteDistricts = ({ commit }, params) => {
  return location.addNewProvince(params).then(res => {
    if (res.status === Status.SUCCESS) {
      console.info(res)
    }
  }).catch(error => {
    throw error
  })
}

// const deleteWards = ({ commit }, ids) => {
//   const param = CommonModelMap.toParam(province)
//   return location.addNewDistrict(param).then(res => {
//     if (res.status === Status.SUCCESS) {
//       commit('ADD_NEW_DISTRICT', res.data)
//     }
//   }).catch(error => {
//     throw error
//   })
// }

export default {
  importLocationData,
  loadListPagingLocation,
  loadListLocation,
  addNewProvince,
  addNewDistrict,
  addNewWard,
  deleteProvinces,
  deleteDistricts
}
