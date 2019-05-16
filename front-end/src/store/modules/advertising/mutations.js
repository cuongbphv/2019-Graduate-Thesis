const CREATE_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

const GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID = (state, payload) => {
  state.classifiedAds = Object.assign({}, payload)
}

export default {
  CREATE_NEW_CLASSIFIED_ADVERTISING,
  GET_CLASSIFIED_ADVERTISING_DETAIL_BY_ID
}
