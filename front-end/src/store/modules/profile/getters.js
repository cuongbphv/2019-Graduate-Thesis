const authorities = state => state.profile.authorities
const setting = state => state.profile.setting
const userId = state => state.profile.userId
const profile = state => state.viewProfile
const currentProfile = state => state.profile

export default {
  authorities,
  setting,
  userId,
  profile,
  currentProfile
}
