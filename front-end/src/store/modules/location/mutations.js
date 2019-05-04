import Location from '../../models/Location'

const IMPORT_LOCATION_DATA = (state, payload) => {}

const GET_PAGING_LOCATION = (state, payload) => {
  const newLocationIds = Array.from(new Set(payload.content.map(location => location.id)))
  const newLocationEntities = payload.content.reduce((locations, location) => {
    locations[location.id] = new Location(location)
    return locations
  }, {})
  state.totalElements = payload.totalElements
  state.totalPages = payload.totalPages
  state.ids = newLocationIds
  state.entities = Object.assign({}, state.entities, newLocationEntities)
}

const ADD_NEW_PROVINCE = (state, payload) => {
}

const ADD_NEW_DISTRICT = (state, payload) => {
}

const ADD_NEW_WARD = (state, payload) => {
}

export default {
  IMPORT_LOCATION_DATA,
  GET_PAGING_LOCATION,
  ADD_NEW_PROVINCE,
  ADD_NEW_DISTRICT,
  ADD_NEW_WARD
}
