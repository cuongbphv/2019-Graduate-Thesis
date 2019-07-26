const topPagingNewClassifiedAds = state => {
  return state.topNewClassifiedAds
}

const listPagingClassifiedAds = state => {
  return state.ids.map(id => state.entities[id])
}

const searchResult = state => {
  return state.searchResult
}

const searchState = state => {
  return state.searchState
}

const topCategoryPost = state => {
  return state.topCategoryPost
}

const savedAds = state => {
  return state.savedAds
}

const classifiedAds = state => {
  return state.classifiedAds
}

const pushPost = state => {
  return state.pushPost
}

export default {
  topPagingNewClassifiedAds,
  listPagingClassifiedAds,
  searchResult,
  searchState,
  topCategoryPost,
  savedAds,
  classifiedAds,
  pushPost
}
