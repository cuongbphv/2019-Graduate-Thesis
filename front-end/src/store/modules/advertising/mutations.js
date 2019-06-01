const CREATE_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

const GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

const GET_PAGING_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
  state.topNewClassifiedAds.content = [...payload.content]
  state.topNewClassifiedAds.totalPages = payload.totalPages
  state.topNewClassifiedAds.totalElements = payload.totalElements
}

export default {
  CREATE_NEW_CLASSIFIED_ADVERTISING,
  GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID,
  GET_PAGING_NEW_CLASSIFIED_ADVERTISING
}
