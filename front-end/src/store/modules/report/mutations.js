import CommonModelMap from '../../models/CommonModelMap'

const GET_PAGING_REPORT = (state, payload) => {
  const newReportIds = Array.from(new Set(payload.content.map(report => report.id)))
  const newReportEntities = payload.content.reduce((reports, report) => {
    reports[report.id] = new CommonModelMap(report)
    return reports
  }, {})
  state.totalElements = payload.totalElements
  state.totalPages = payload.totalPages
  state.ids = newReportIds
  state.entities = Object.assign({}, state.entities, newReportEntities)
}

export default {
  GET_PAGING_REPORT
}
