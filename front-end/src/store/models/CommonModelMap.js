export default class CommonModelMap {
  constructor(data = {}) {
    Object.keys(data).map(key => {
      this[key] = data[key]
    })
  }
}
