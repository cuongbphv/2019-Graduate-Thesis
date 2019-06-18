import CommonModelMap from '../../models/CommonModelMap'
import Advertising from '../../models/Advertising'
import advertising from '@/api/advertising'
import { Status } from '@/utils/constants'

const removeTempImage = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.removeTempImage(param).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const uploadTempImage = ({ commit }, params) => {
  return advertising.uploadTempImage(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const changeStatusAds = ({ commit }, payload) => {
  return advertising.changeStatusAds(payload.id, payload.status).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const addNewAdvertising = ({ commit }, params) => {
  const ads = Advertising.toNewAdvertising(params)
  return advertising.createNewAdvertising(ads).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('CREATE_NEW_CLASSIFIED_ADVERTISING', res.data)
      return res.data.id
    }
  }).catch(error => {
    throw error
  })
}

const getClassifiedAdsDetail = ({ commit }, id) => {
  return advertising.getAdvertisingDetail(id).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const getPagingNewClassifiedAds = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.getNewClassifiedAds(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAGING_NEW_CLASSIFIED_ADVERTISING', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const getPagingUserHistoryClassifiedAds = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.getUserHistoryClassifiedAds(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAGING_NEW_CLASSIFIED_ADVERTISING', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const fullTextSearch = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.fullTextSearch(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('FULL_TEXT_SEARCH', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const saveSearchState = ({ commit }, state) => {
  commit('SAVE_SEARCH_STATE', state)
}

export default {
  removeTempImage,
  uploadTempImage,
  changeStatusAds,
  addNewAdvertising,
  getClassifiedAdsDetail,
  getPagingNewClassifiedAds,
  getPagingUserHistoryClassifiedAds,
  fullTextSearch,
  saveSearchState
}
