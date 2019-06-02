import Location from '../../models/Location'

const CREATE_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
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

export default {
  CREATE_NEW_CLASSIFIED_ADVERTISING,
  GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID,
  GET_PAGING_NEW_CLASSIFIED_ADVERTISING
}
