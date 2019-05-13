import CommonModelMap from '../../models/CommonModelMap'
import Advertising from '../../models/Advertising'
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

const addNewAdvertising = ({ commit }, params) => {
  const ads = Advertising.toNewAdvertising(params)
  return advertising.createNewAdvertising(ads).then(res => {
    return res
  }).catch(error => {
    throw error
  })
}

export default {
  removeTempImage,
  uploadTempImage,
  addNewAdvertising
}
