import CommonModelMap from '../../models/CommonModelMap'
import advertising from '@/api/advertising'

const removeTempImage = ({ commit }, params) => {
  const param = CommonModelMap.toParam(params)
  return advertising.removeTempImage(param).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

const uploadTempImage = ({ commit }, params) => {
  return advertising.uploadTempImage(params).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

export default {
  removeTempImage,
  uploadTempImage
}
