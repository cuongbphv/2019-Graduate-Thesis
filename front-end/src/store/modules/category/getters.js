const listCategory = state => {
  return state.ids.map(id => state.entities[id])
}
const metadata = state => {
  return state.metadata
}
const metadatas = state => {
  return state.metadatas
}

export default {
  listCategory,
  metadata,
  metadatas
}
