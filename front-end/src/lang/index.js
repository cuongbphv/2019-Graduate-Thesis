import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import elementEnLocale from 'element-ui/lib/locale/lang/en' // element-ui lang
import elementViLocale from 'element-ui/lib/locale/lang/vi' // element-ui lang
import en from './en'
import vi from './vi'

Vue.use(VueI18n)

const messages = {
  en: {
    ...en,
    ...elementEnLocale
  },
  vi: {
    ...vi,
    ...elementViLocale
  }
}

const i18n = new VueI18n({
  // set locale
  locale: Cookies.get('language') || 'vi',
  // set locale messages
  messages
})

export default i18n
