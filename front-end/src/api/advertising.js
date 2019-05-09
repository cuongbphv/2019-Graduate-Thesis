import Api from '@/api/api'
import { API } from '@/utils/constants'

export default {
  uploadTempImage(params) {
    return Api.post(API.UPLOAD_TEMP_IMAGE, params).then(res => res.data)
  },
  removeTempImage(params) {
    return Api.delete(API.REMOVE_TEMP_IMAGE, { params: params }).then(res => res.data)
  }
}
