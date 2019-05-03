const listPagingLocation = state => {
  return state.ids.map(id => state.entities[id])
}

const listDistrictByProvinceId = state => id => {
  return state.entities[id].districts
}

const listWardByDistrictId = state => (provinceId, districtId) => {
  return state.entities[provinceId].districts.find(district => district.id === districtId).wards
}

export default {
  listPagingLocation,
  listDistrictByProvinceId,
  listWardByDistrictId
}
