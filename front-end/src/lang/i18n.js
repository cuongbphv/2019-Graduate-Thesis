import Vue from 'vue'
import VueI18n from 'vue-i18n'
import viLang from './vi.json'
import enLang from './en.json'

Vue.use(VueI18n)

const langs = {
  vi: viLang,
  en: enLang
}

const i18n = new VueI18n({
  locale: 'vi', // default language
  langs,
  fallbackLocale: 'en'
})

export default i18n
