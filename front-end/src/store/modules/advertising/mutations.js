const CREATE_NEW_CLASSIFIED_ADVERTISING = (state, payload) => {
  state.addresses = Object.assign([], state.classifiedAds, payload)
}

export default {
  CREATE_NEW_CLASSIFIED_ADVERTISING
}
