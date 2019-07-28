import report from '@/api/report'
import { Status } from '@/utils/constants'
import CommonModelMap from '../../models/CommonModelMap'

const createNewReport = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return report.createNewReport(param).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const changeStatusReport = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return report.changeStatusReport(param).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const getNumberOfReport = ({ commit }, id) => {
  return report.getNumberOfReport({
    id: id
  }).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const getPagingReport = ({ commit }, query) => {
  const param = CommonModelMap.toParam(query)
  return report.getListReport(param).then(res => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAGING_REPORT', res.data)
    }
  }).catch(error => {
    throw error
  })
}

export default {
  createNewReport,
  changeStatusReport,
  getNumberOfReport,
  getPagingReport
}
