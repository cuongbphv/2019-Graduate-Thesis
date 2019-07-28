import payment from '@/api/payment'
import { Status } from '@/utils/constants'

const createPayment = ({ commit }, params) => {
  return payment.createPayment(params.amount, params.coin).then(res => {
    if (res.status === Status.SUCCESS) {
      return res.data
    }
    // this.$message.error(this.$t('status.' + res.status))
  }).catch(error => {
    throw error
  })
}

const getAccount = ({ commit }) => {
  return payment.getAccountBalance().then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_ACCOUNT', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getPaymentHistory = ({ commit }) => {
  return payment.getPaymentHistory().then((res) => {
    if (res.status === Status.SUCCESS) {
      commit('GET_PAYMENT_HISTORY', res.data)
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const confirmPayment = ({ commit }, params) => {
  return payment.confirmPayment(params.paymentId, params.payerId, params.id).then((res) => {
    if (res.status === Status.SUCCESS) {
      return res.data
    }
  }).catch(error => {
    throw error
  })
}

const getCurrencyRate = ({ commit }) => {
  return payment.getCurrencyRate().then(res => {
    commit('CURRENCY_RATE', res['VND_USD'])
    return res['VND_USD']
  }).catch(error => {
    throw error
  })
}

export default {
  createPayment,
  getAccount,
  confirmPayment,
  getCurrencyRate,
  getPaymentHistory
}
