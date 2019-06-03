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

const addNewProvince = ({ commit }, params) => {
  const param = Location.toProvinceParam(params)
  return location.addNewProvince(param).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const updateProvince = ({ commit }, params) => {
  const param = Location.toProvinceParam(params)
  return location.updateProvince(params.id, param).then(res => {
    return res
  })
}

const addNewDistrict = ({ commit }, params) => {
  const param = Location.toListDistrictParam(params)
  return location.addNewDistrict(param).then(res => {
    if (res.status === Status.SUCCESS) {
      return res
    }
  }).catch(error => {
    throw error
  })
}

const updateDistrict = ({ commit }, params) => {
  const param = Location.toUpdateDistrictParam(params)
  return location.updateDistrict(params.id, param).then(res => {
    return res
  })
}

const addNewWard = ({ commit }, params) => {
  const param = Location.toListWardParam(params)
  return location.addNewWard(param).then(res => {
    if (res.status === Status.SUCCESS) {
      return res
    }
  }).catch(error => {
    throw error
  })
}

const updateWard = ({ commit }, params) => {
  const param = Location.toUpdateWardParam(params)
  return location.updateWard(params.id, param).then(res => {
    return res
  })
}

const deleteProvinces = ({ commit }, params) => {
  return location.deleteProvinces(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const deleteDistricts = ({ commit }, params) => {
  return location.deleteDistricts(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const deleteWards = ({ commit }, params) => {
  return location.deleteWards(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

export default {
  importLocationData,
  loadListPagingLocation,
  loadListLocation,
  addNewProvince,
  addNewDistrict,
  addNewWard,
  updateProvince,
  updateDistrict,
  updateWard,
  deleteProvinces,
  deleteDistricts,
  deleteWards
}
