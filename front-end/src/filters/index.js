import i18n from '../lang/index'

// set function parseTime,formatTime to filter
export { parseTime, formatTime } from '@/utils'

function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

/* 数字 格式化*/
export function numberFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value + 0.1).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
    }
  }
  return num.toString()
}

export function toThousandFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

export function currency(value, symbol, decimals, options) {
  var thousandsSeparator, symbolOnLeft, spaceBetweenAmountAndSymbol
  var digitsRE = /(\d{3})(?=\d)/g
  options = options || {}
  value = parseFloat(value)
  if (!isFinite(value) || (!value && value !== 0)) return ''
  symbol = symbol != null ? symbol : '$'
  decimals = decimals != null ? decimals : 2
  thousandsSeparator = options.thousandsSeparator != null ? options.thousandsSeparator : ','
  symbolOnLeft = options.symbolOnLeft != null ? options.symbolOnLeft : true
  spaceBetweenAmountAndSymbol = options.spaceBetweenAmountAndSymbol != null ? options.spaceBetweenAmountAndSymbol : false
  var stringified = Math.abs(value).toFixed(decimals)
  stringified = options.decimalSeparator
    ? stringified.replace('.', options.decimalSeparator)
    : stringified
  var _int = decimals
    ? stringified.slice(0, -1 - decimals)
    : stringified
  var i = _int.length % 3
  var head = i > 0
    ? (_int.slice(0, i) + (_int.length > 3 ? thousandsSeparator : ''))
    : ''
  var _float = decimals
    ? stringified.slice(-1 - decimals)
    : ''
  symbol = spaceBetweenAmountAndSymbol
    ? (symbolOnLeft ? symbol + ' ' : ' ' + symbol)
    : symbol
  symbol = symbolOnLeft
    ? symbol + head +
    _int.slice(i).replace(digitsRE, '$1' + thousandsSeparator) + _float
    : head +
    _int.slice(i).replace(digitsRE, '$1' + thousandsSeparator) + _float + symbol
  var sign = value < 0 ? '-' : ''
  return sign + symbol
}

export function formatDate(strDate) {
  // eslint-disable-next-line
  let options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }

  const date = new Date(strDate)
  const day = date.getDate()
  let month = date.getMonth() + 1
  const year = date.getFullYear()

  if (month < 10) month = '0' + month

  // eslint-disable-next-line
  if (i18n.locale === 'vi') {
    return day + '/' + month + '/' + year
  } else if (i18n.locale === 'en') {
    return month + '/' + day + '/' + year
    // return new Date(date).toLocaleDateString('en-US')
  }
}

export function formatName(firstName, lastName) {
  if (i18n.locale === 'vi') {
    return firstName + ' ' + lastName
  } else if (i18n.locale === 'en') {
    return lastName + ' ' + firstName
  }
}

export function statusFilter(status) {
  const statusMap = {
    3: 'warning',
    2: 'info',
    1: 'primary',
    0: 'danger'
  }
  return statusMap[status] || statusMap[2]
}

