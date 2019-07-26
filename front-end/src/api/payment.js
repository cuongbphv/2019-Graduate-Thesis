import Api from '../api/api'
import currency from '../api/currency'
import { API } from '../utils/constants'

export default {
  getAccountBalance() {
    return Api.get(API.PAYMENT).then(res => res.data)
  },
  getPaymentHistory() {
    return Api.get(API.PAYMENT + '/history').then(res => res.data)
  },
  createPayment(amount, coin) {
    return Api.post(API.PAYMENT + `?price=${amount.toFixed(2)}&coin=${coin}`).then(res => res.data)
  },
  confirmPayment(paymentId, payerId, id) {
    return Api.get(API.PAYMENT + '/success', { params: {
      paymentId: paymentId, payerId: payerId, id: id }}).then(res => res.data)
  },
  getCurrencyRate() {
    return currency.get(API.CONVERT, { params: {
      q: 'VND_USD',
      compact: 'ultra',
      apiKey: '29247a2345e19056dad1'
    }}).then(res => res.data)
  }
}
