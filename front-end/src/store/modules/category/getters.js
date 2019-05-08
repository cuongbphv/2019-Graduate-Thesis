const listCategory = state => {
  return state.ids.map(id => state.entities[id])
}

export default {
  listCategory
}
