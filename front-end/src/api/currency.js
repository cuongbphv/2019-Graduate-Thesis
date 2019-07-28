import axios from 'axios'

const currency =
  axios.create({
    baseURL: 'https://free.currconv.com/api/v7',
    withCredentials: false,
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json'
    },
    timeout: 5000 // request timeout
  })

export default currency
