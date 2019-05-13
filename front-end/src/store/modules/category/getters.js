const listCategory = state => {
  return state.ids.map(id => state.entities[id])
}
const metadata = state => {
  return state.metadata
}

export default {
  listCategory,
  metadata
}
