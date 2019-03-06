import Api from '../api/api'

export default {
  signin (params) {
    return Api.post('/sign_in', params).then(res => res.data)
  },
  signup (params) {
    return Api.post('/sign_up', params)
  }
}
