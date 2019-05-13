import Api from '@/api/api'
import { API } from '@/utils/constants'

export default {
  createNewAdvertising(params) {
    return Api.post(API.ADVERTISING, params).then(res => res.data)
  },
  uploadTempImage(params) {
    return Api.post(API.UPLOAD_TEMP_IMAGE, params).then(res => res.data)
  },
  removeTempImage(params) {
    return Api.delete(API.REMOVE_TEMP_IMAGE, { params: params }).then(res => res.data)
  }
}