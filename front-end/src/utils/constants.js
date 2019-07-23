const API = {
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  OTP: '/auth/otp',
  INIT_DATA: '/user/me',
  CHANGE_PASSWORD: '/user/password',
  USER: '/user',
  PASSWORD: '/password',
  BAN: '/ban',
  UNLOCK: '/unlock',
  SYS_ADMIN_LIST_USER: '/user/list',
  ADMIN_LIST_USER: '/user',
  PROFILE: '/profile',
  FOLLOW: '/follow',
  RATING: '/rating',
  STATISTIC: '/statistic',
  STATISTICS: '/statistics',
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
  METADATAS: '/metadatas',
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
  SAVES: '/saves',
  STATUS: '/status',
  TOP_CATEGORY_POST: '/advertising/top_category_post',
  // ROLE_PERMISSION APIs
  ROLE: '/role',
  PERMISSION: '/permission',
  // CHAT
  CHAT: '/chats',
  NOTIFICATION: '/notifications',
  MESSAGE: '/messages',
  // REPORT
  REPORT: '/report',
  REPORT_LIST: '/report/list',
  NUMBER_OF_REPORT: '/report/number'
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
