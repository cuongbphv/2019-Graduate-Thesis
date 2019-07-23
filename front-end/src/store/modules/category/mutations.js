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
const GET_LIST_CATEGORY = (state, payload) => {
  const newCategoryIds = Array.from(new Set(payload.map(category => category.id)))
  const newCategoryEntities = payload.reduce((categorys, category) => {
    categorys[category.id] = new CommonModelMap(category)
    return categorys
  }, {})
  state.ids = newCategoryIds
  state.entities = Object.assign({}, state.entities, newCategoryEntities)
}
const GET_METADATA = (state, payload) => {
  state.metadata = Object.assign([], payload)
}
const GET_METADATAS = (state, payload) => {
  state.metadatas = Object.assign([], payload)
}
export default {
  GET_PAGING_CATEGORY,
  GET_LIST_CATEGORY,
  GET_METADATA,
  GET_METADATAS
}
