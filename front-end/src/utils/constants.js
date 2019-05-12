const API = {
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  INIT_DATA: '/user/me',
  CHANGE_PASSWORD: '/user/password',
  PROFILE: '/profile',
  // Location APIs
  LOCATION: '/location',
  GET_LIST_LOCATION: '/location/list',
  PROVINCE: '/location/province',
  DISTRICT: 'location/district',
  WARD: '/location/ward',
  IMPORT_LOCATION: '/location/import',
  // CATEGORY APIs
  CATEGORY: '/category',
  METADATA: '/metadata',
  FILTER: '/filter',
  // ADVERTISING APIs
  ADVERTISING: '/advertising',
  UPLOAD_TEMP_IMAGE: '/advertising/upload_temp_image',
  REMOVE_TEMP_IMAGE: '/advertising/remove_temp_image'
}

const Status = {
  SUCCESS: 200,
  CREATED: 201
}

const AdsLocationType = {
  NEW_LOCATION: 1,
  EXISTED_LOCATION: 2
}

export {
  API,
  Status,
  AdsLocationType
}
