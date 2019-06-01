const listLocation = state => {
  return state.ids.map(id => state.entities[id])
}

const listDistrictByProvinceId = state => id => {
  if (state.entities[id]) {
    return state.entities[id].districts || []
  }
}

const listWardByDistrictId = state => (provinceId, districtId) => {
  if (state.entities[provinceId]) {
    const district = state.entities[provinceId].districts.find(district => district.id === districtId)
    return district ? district.wards : []
  }
}

export default {
  listLocation,
  listDistrictByProvinceId,
  listWardByDistrictId
}
