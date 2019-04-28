import CommonModelMap from '../../models/CommonModelMap'

const IMPORT_LOCATION_DATA = (state, payload) => {
  const newLocationIds = Array.from(new Set(payload.content.map(location => location.id)))
  const newLocationEntities = payload.content.reduce((locations, location) => {
    locations[location.id] = new CommonModelMap(location)
    return locations
  }, {})
  state.total = payload.total_elements
  state.ids = newLocationIds
  state.entities = Object.assign({}, state.entities, newLocationEntities)
}

export default {
  IMPORT_LOCATION_DATA
}
