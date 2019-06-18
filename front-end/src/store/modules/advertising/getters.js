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

export default {
  topPagingNewClassifiedAds,
  listPagingClassifiedAds,
  searchResult,
  searchState
}
