import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getListReport(params) {
    return Api.get(API.REPORT_LIST, { params: params }).then(res => res.data)
  },
  createNewReport(params) {
    return Api.post(API.REPORT, params).then(res => res.data)
  },
  changeStatusReport(params) {
    return Api.put(API.REPORT, params).then(res => res.data)
  },
  getNumberOfReport(params) {
    return Api.get(API.NUMBER_OF_REPORT, { params: params }).then(res => res.data)
  }
}
