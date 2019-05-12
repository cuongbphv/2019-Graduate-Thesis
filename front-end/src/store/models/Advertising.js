import snakeCaseKeys from 'snakecase-keys'

export default class Advertising {
  constructor(data = {}) {
    delete data._class // remove class properties from backend
    Object.keys(data).map(key => {
      this[key] = data[key]
    })
  }

  static toNewAdvertising(param) {
    if (param.location) {
      delete param.location.province.districts
      delete param.location.district.wards
      param.province = Object.assign({}, param.location.province)
      param.district = Object.assign({}, param.location.district)
      param.ward = Object.assign({}, param.location.ward)
      delete param.location
    }
    return snakeCaseKeys(param)
  }
}
