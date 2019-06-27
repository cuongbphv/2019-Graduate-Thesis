const listReports = state => {
  return state.ids.map(id => state.entities[id])
}

const getDetailReport = state => id => {
  if (state.entities[id]) {
    return state.entities[id]
  }
}

export default {
  listReports,
  getDetailReport
}
