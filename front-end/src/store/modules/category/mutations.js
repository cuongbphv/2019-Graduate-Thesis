import CommonModelMap from '../../models/CommonModelMap'

const GET_PAGING_CATEGORY = (state, payload) => {
  const newCategoryIds = Array.from(new Set(payload.content.map(category => category.id)))
  const newCategoryEntities = payload.content.reduce((categorys, category) => {
    categorys[category.id] = new CommonModelMap(category)
    return categorys
  }, {})
  state.totalElements = payload.totalElements
  state.totalPages = payload.totalPages
  state.ids = newCategoryIds
  state.entities = Object.assign({}, state.entities, newCategoryEntities)
}

export default {
  GET_PAGING_CATEGORY
}
