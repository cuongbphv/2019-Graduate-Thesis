const API = {
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  OTP: '/auth/otp',
  INIT_DATA: '/user/me',
  CHANGE_PASSWORD: '/user/password',
  SYS_ADMIN_LIST_USER: '/user/list',
  PROFILE: '/profile',
  FOLLOW: '/follow',
  RATING: '/rating',
  STATISTIC: '/statistic',
  UPDATE_PERSONAL_PERMISSION: '/profile/permissions',
  // Location APIs
  LOCATION: '/location',
  GET_LIST_LOCATION: '/location/list',
  PROVINCE: '/location/province',
  DISTRICT: 'location/district',
  WARD: '/location/ward',
  IMPORT_LOCATION: '/location/import',
  // CATEGORY APIs
  CATEGORY: '/category',
  LIST_CATEGORY: '/category/list',
  BREADCRUMBS: '/category/breadcrumbs',
  METADATA: '/metadata',
  FILTER: '/filter',
  // ADDRESS APIs
  ADDRESS: '/address',
  // ADVERTISING APIs
  ADVERTISING: '/advertising',
  NEW_ADVERTISING: '/advertising/new',
  USER_HISTORY_ADS: '/advertising/history',
  UPLOAD_TEMP_IMAGE: '/advertising/upload_temp_image',
  REMOVE_TEMP_IMAGE: '/advertising/remove_temp_image',
  FULL_TEXT_SEARCH: '/advertising/search',
  TOP_CATEGORY_POST: '/advertising/top_category_post',
  // ROLE_PERMISSION APIs
  ROLE: '/role',
  PERMISSION: '/permission',
  // CHAT
  CHAT: '/chats',
  MESSAGE: '/messages'
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
