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

const updateAdvertising = ({ commit }, params) => {
  const ads = Advertising.toNewAdvertising(params)
  return advertising.updateAdvertising(params.id, ads).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('UPDATE_CLASSIFIED_ADVERTISING', res.data)
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
  return advertising.getUserHistoryClassifiedAds(param, params.userId).then(res => {
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

const getTopCategoryPost = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.getTopCategoryPost(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('TOP_CATEGORY_POST', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const saveSearchState = ({ commit }, state) => {
  commit('SAVE_SEARCH_STATE', state)
}

const changeTradingStatusAds = ({ commit }, payload) => {
  return advertising.changeTradingStatusAds(payload.id, payload.status).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const getSavedAds = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.getSaveAds(param, params.id).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_SAVED_ADS', res.data)
    }
  }).catch(error => {
    throw error
  })
}

const saveAds = ({ commit }, id) => {
  return advertising.saveAds(id).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const removeSaveAds = ({ commit }, id) => {
  return advertising.removeSaveAds(id).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const pushAdvertising = ({ commit }, params) => {
  return advertising.pushAdvertising(params.id, params.period).then(res => {
    if (res.status === Status.SUCCESS) {
      return res.data.id
    }
  }).catch(error => {
    throw error
  })
}

const getPushPost = ({ commit }, categoryId) => {
  return advertising.getPushPost({ category_id: categoryId }).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('PUSH_POST', res.data)
    }
  }).catch(error => {
    throw error
  })
}

export default {
  removeTempImage,
  uploadTempImage,
  changeStatusAds,
  addNewAdvertising,
  updateAdvertising,
  getClassifiedAdsDetail,
  getPagingNewClassifiedAds,
  getPagingUserHistoryClassifiedAds,
  fullTextSearch,
  saveSearchState,
  getTopCategoryPost,
  changeTradingStatusAds,
  getSavedAds,
  saveAds,
  removeSaveAds,
  pushAdvertising,
  getPushPost
}
