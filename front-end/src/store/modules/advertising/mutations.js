import Location from '../../models/Location'

const CREATE_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

const UPDATE_CLASSIFIED_ADVERTISING = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

const GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

const GET_PAGING_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
  const newAdsIds = Array.from(new Set(payload.content.map(ads => ads.id)))
  const newAdsEntities = payload.content.reduce((ads, post) => {
    ads[post.id] = new Location(post)
    return ads
  }, {})
  state.totalElements = payload.totalElements
  state.totalPages = payload.totalPages
  state.ids = newAdsIds
  state.entities = Object.assign({}, state.entities, newAdsEntities)
}

const FULL_TEXT_SEARCH = (state, payload) => {
  state.searchResult.content = [...payload.content]
  state.searchResult.totalRecord = payload.totalRecord
  state.searchResult.pageSize = payload.pageSize
  state.searchResult.pageNumber = payload.pageNumber
}

const TOP_CATEGORY_POST = (state, payload) => {
  state.topCategoryPost = Object.assign([], payload.content)
}

const SAVE_SEARCH_STATE = (state, payload) => {
  state.searchState = Object.assign({}, payload)
}

const GET_SAVED_ADS = (state, payload) => {
  state.savedAds = Object.assign([], payload)
}

const PUSH_POST = (state, payload) => {
  state.pushPost = Object.assign({}, payload)
}

export default {
  CREATE_NEW_CLASSIFIED_ADVERTISING,
  UPDATE_CLASSIFIED_ADVERTISING,
  GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID,
  GET_PAGING_NEW_CLASSIFIED_ADVERTISING,
  FULL_TEXT_SEARCH,
  SAVE_SEARCH_STATE,
  TOP_CATEGORY_POST,
  GET_SAVED_ADS,
  PUSH_POST
}
