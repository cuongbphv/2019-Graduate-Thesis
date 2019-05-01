export default class CommonModelMap {
  constructor(data = {}) {
    Object.keys(data).map(key => {
      this[key] = data[key]
      this.edit = false // for edit inline row
      if (data.districts) {
        data.districts.map(district => {
          district.edit = false
          if (district.wards) {
            district.wards.map(ward => {
              ward.edit = false
            })
          }
        })
      }
    })
  }
}
