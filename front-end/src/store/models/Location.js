import snakeCaseKeys from 'snakecase-keys'
import { toSlug } from '@/utils'

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

  static toProvinceParam(param) {
    if (Object.keys(param).length > 0) {
      param.slug = toSlug(param.name)
      if (param.districts) {
        param.districts.map(district => {
          delete district._hash
          district.slug = toSlug(district.name)
          if (district.wards) {
            district.wards.map(ward => {
              delete ward._hash
              ward.slug = toSlug(ward.name)
            })
          }
        })
      }
    }
    return snakeCaseKeys(param)
  }

  static toListDistrictParam(param) {
    if (param.length > 0) {
      param.map(district => {
        delete district._hash
        district.slug = toSlug(district.name)
        if (district.wards) {
          district.wards.map(ward => {
            delete ward._hash
            ward.slug = toSlug(ward.name)
          })
        }
      })
    }
    return snakeCaseKeys(param)
  }

  static toListWardParam(param) {
    if (param.length > 0) {
      param.map(ward => {
        delete ward._hash
        ward.slug = toSlug(ward.name)
      })
    }
    return snakeCaseKeys(param)
  }
}
