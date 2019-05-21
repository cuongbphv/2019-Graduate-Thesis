
export default class Permission {
  constructor(data = {}) {
    delete data._class // remove class properties from backend
    Object.keys(data).map(key => {
      this[key] = data[key]
      this.edit = false // for edit inline row
    })
  }
}
