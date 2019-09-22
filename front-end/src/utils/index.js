import i18n from '@/lang'

// define constant
const SECOND_MILLIS = 1000
const MINUTE_MILLIS = 60 * SECOND_MILLIS
const HOUR_MILLIS = 60 * MINUTE_MILLIS
const DAY_MILLIS = 24 * HOUR_MILLIS

export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

export function formatTime(time, option) {
  time = +time * 1000
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

export function getQueryObject(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * @param {Sting} input value
 * @returns {number} output value
 */
export function byteLength(str) {
  // returns the byte length of an utf8 string
  let s = str.length
  for (var i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i)
    if (code > 0x7f && code <= 0x7ff) s++
    else if (code > 0x7ff && code <= 0xffff) s += 2
    if (code >= 0xDC00 && code <= 0xDFFF) i--
  }
  return s
}

export function cleanArray(actual) {
  const newArray = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

export function param(json) {
  if (!json) return ''
  return cleanArray(
    Object.keys(json).map(key => {
      if (json[key] === undefined) return ''
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
    })
  ).join('&')
}

export function param2Obj(url) {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse(
    '{"' +
      decodeURIComponent(search)
        .replace(/"/g, '\\"')
        .replace(/&/g, '","')
        .replace(/=/g, '":"')
        .replace(/\+/g, ' ') +
      '"}'
  )
}

export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

export function objectMerge(target, source) {
  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach(property => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

export function toggleClass(element, className) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) +
      classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

export function getTime(type) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}

export function debounce(func, wait, immediate) {
  let timeout, args, context, timestamp, result
  const later = function() {
    const last = +new Date() - timestamp
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function(...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

export function deepClone(source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'deepClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach(keys => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

export function uniqueArr(arr) {
  return Array.from(new Set(arr))
}

export function createUniqueString() {
  const timestamp = +new Date() + ''
  const randomNum = parseInt((1 + Math.random()) * 65536) + ''
  return (+(randomNum + timestamp)).toString(32)
}

export function compareExpression(key, order) {
  return (a, b) => {
    if (!a.prototype.hasOwnProperty.call(key) || !b.prototype.hasOwnProperty.call(key)) return 0
    const comparison = a[key].localeCompare(b[key])
    return order ? (comparison * -1) : comparison
  }
}

export function toSlug(str) {
  // Chuyển hết sang chữ thường
  str = str.toLowerCase()
  // xóa dấu
  str = str.replace(/([àáạảãâầấậẩẫăằắặẳẵ])/g, 'a')
  str = str.replace(/([èéẹẻẽêềếệểễ])/g, 'e')
  str = str.replace(/([ìíịỉĩ])/g, 'i')
  str = str.replace(/([òóọỏõôồốộổỗơờớợởỡ])/g, 'o')
  str = str.replace(/([ùúụủũưừứựửữ])/g, 'u')
  str = str.replace(/([ỳýỵỷỹ])/g, 'y')
  str = str.replace(/(đ)/g, 'd')
  // Xóa ký tự đặc biệt
  str = str.replace(/([^0-9a-z-\s])/g, '')
  // Xóa khoảng trắng thay bằng ký tự -
  str = str.replace(/(\s+)/g, '-')
  // xóa phần dự - ở đầu
  str = str.replace(/^-+/g, '')
  // xóa phần dư - ở cuối
  str = str.replace(/-+$/g, '')
  // return
  return str
}

export function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = Math.random() * 16 | 0
    const v = (c === 'x') ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

export function getTimeAgo(date) {
  // convert date to time stamp
  const timestamp = new Date(date).getTime()
  // get time stamp by timezone of local
  const timeZoneStamp = -1 * (new Date().getTimezoneOffset()) * 60 * 1000
  const now = new Date().getTime() - timeZoneStamp
  // validate timestamp
  if (timestamp > now || timestamp <= 0) return null
  // calculate diff between time
  const diff = now - timestamp
  // return result by diff
  if (diff < MINUTE_MILLIS) {
    return i18n.t('time_ago.now')
  } else if (diff < 2 * MINUTE_MILLIS) {
    return i18n.t('time_ago.a_minute_ago')
  } else if (diff < 50 * MINUTE_MILLIS) {
    return Math.floor(diff / MINUTE_MILLIS) + ' ' + i18n.t('time-ago.minute_ago')
  } else if (diff < 90 * MINUTE_MILLIS) {
    return i18n.t('time_ago.a_hour_ago')
  } else if (diff < 24 * HOUR_MILLIS) {
    return Math.floor(diff / HOUR_MILLIS) + ' ' + i18n.t('time-ago.hour_ago')
  } else if (diff < 48 * HOUR_MILLIS) {
    return i18n.t('time_ago.a_day_ago')
  } else {
    return Math.floor(diff / DAY_MILLIS) + ' ' + i18n.t('time_ago.day_ago')
  }
}
