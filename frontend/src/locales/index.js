import { createI18n } from 'vue-i18n'
import zh from './zh'
import en from './en'

// 获取浏览器语言
function getBrowserLanguage() {
  const language = navigator.language.toLowerCase()
  if (language.includes('zh')) {
    return 'zh'
  }
  return 'en'
}

// 从 localStorage 获取语言设置，如果没有则使用浏览器语言
const savedLanguage = localStorage.getItem('language')
const defaultLanguage = savedLanguage || getBrowserLanguage()

const i18n = createI18n({
  legacy: false, // 使用 Composition API 模式
  locale: defaultLanguage,
  fallbackLocale: 'zh',
  messages: {
    zh,
    en
  }
})

export default i18n
