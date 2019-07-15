import Api from '../api/api'
import { API } from '../utils/constants'

export default {
  getStatistics() {
    return Api.get(API.STATISTICS).then(res => res.data)
  }
}
