const API = {
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  INIT_DATA: '/user/me',
  CHANGE_PASSWORD: '/user/password',
  PROFILE: '/profile',
  // Location APIs
  LOCATION: '/location',
  PROVINCE: '/location/province',
  DISTRICT: 'location/district',
  WARD: '/location/ward',
  IMPORT_LOCATION: '/location/import'
}

const Status = {
  SUCCESS: 200,
  CREATED: 201
}

export {
  API,
  Status
}
