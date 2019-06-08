const topPagingNewClassifiedAds = state => {
  return state.topNewClassifiedAds
}

const listPagingClassifiedAds = state => {
  return state.ids.map(id => state.entities[id])
}

const searchResult = state => {
  return state.searchResult
}

export default {
  topPagingNewClassifiedAds,
  listPagingClassifiedAds,
  searchResult
}
