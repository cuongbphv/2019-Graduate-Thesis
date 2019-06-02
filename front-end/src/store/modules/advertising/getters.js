const topPagingNewClassifiedAds = state => {
  return state.topNewClassifiedAds
}

const listPagingClassifiedAds = state => {
  return state.ids.map(id => state.entities[id])
}

export default {
  topPagingNewClassifiedAds,
  listPagingClassifiedAds
}
