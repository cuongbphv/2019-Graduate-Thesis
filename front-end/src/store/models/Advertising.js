import snakeCaseKeys from 'snakecase-keys'

export default class Advertising {
  constructor(data = {}) {
    delete data._class // remove class properties from backend
    Object.keys(data).map(key => {
      this[key] = data[key]
    })
  }

  static toNewAdvertising(param) {
    const requestParam = {}
    // handle for location
    if (param.location.locationType === 1) {
      requestParam.locationType = 1
      requestParam.provinceId = param.location.province.id
      requestParam.districtId = param.location.district.id
      requestParam.wardId = param.location.ward.id
    } else if (param.location.locationType === 2) {
      requestParam.locationType = 2
      requestParam.addressId = param.location.selectedLocation
    }
    // handle for image remove raw file and response
    requestParam.images = param.images.map(img => {
      return {
        name: img.name,
        percentage: img.percentage,
        size: img.size,
        status: img.status,
        uid: img.uid,
        url: img.url
      }
    })
    // handle for additional information
    requestParam.additionalInfo = {
      title: param.additionalInfo.title,
      description: param.additionalInfo.description,
      adsType: param.additionalInfo.adsType,
      maxPrice: param.additionalInfo.maxPrice || 0,
      price: param.additionalInfo.price || 0
    }
    // handle category
    requestParam.breadcrumbs = param.category.breadCrumb.map(cat => cat.id)
    requestParam.metadata = param.category.postMetadata
    return snakeCaseKeys(requestParam)
  }
}
