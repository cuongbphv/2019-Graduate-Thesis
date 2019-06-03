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

const FULL_TEXT_SEARCH = (state, payload) => {
  state.searchResult.content = [...payload.content]
  state.searchResult.totalRecord = payload.totalRecord
  state.searchResult.pageSize = payload.pageSize
  state.searchResult.pageNumber = payload.pageNumber
}

export default {
  CREATE_NEW_CLASSIFIED_ADVERTISING,
  GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID,
  GET_PAGING_NEW_CLASSIFIED_ADVERTISING,
  FULL_TEXT_SEARCH
}
