import snakeCaseKeys from 'snakecase-keys'

export default class CommonModelMap {
  constructor(data = {}) {
    delete data._class // remove class properties from backend
    Object.keys(data).map(key => {
      this[key] = data[key]
    })
  }

  static toParam(param) {
    return snakeCaseKeys(param)
  }
}
