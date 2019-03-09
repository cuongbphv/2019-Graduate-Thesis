import axios from 'axios'
import interceptors from './interceptors'

const Api =
  axios.create({
    baseURL: process.env.VUE_APP_BASE_URL,
    withCredentials: false,
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json'
    }
  })

interceptors.setup(Api)
interceptors.checkToken(Api)
interceptors.showSpinner(Api)
interceptors.hideSpinner(Api)
interceptors.checkError(Api)

export default Api
