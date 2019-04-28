import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  importLocationData(params) {
    return Api.post(API.IMPORT_LOCATION, params).then(res => res.data)
  }
}
