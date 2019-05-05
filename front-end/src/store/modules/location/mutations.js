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

export default {
  IMPORT_LOCATION_DATA,
  GET_PAGING_LOCATION
}
