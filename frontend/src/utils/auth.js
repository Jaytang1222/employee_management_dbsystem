const TOKEN_KEY = 'ems_token'
const USER_INFO_KEY = 'ems_user_info'

// Token 管理
export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  sessionStorage.setItem(TOKEN_KEY, token)
}

export function removeToken() {
  sessionStorage.removeItem(TOKEN_KEY)
}

// 用户信息管理
export function getUserInfo() {
  const userInfo = sessionStorage.getItem(USER_INFO_KEY)
  return userInfo ? JSON.parse(userInfo) : null
}

export function setUserInfo(userInfo) {
  sessionStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo))
}

export function removeUserInfo() {
  sessionStorage.removeItem(USER_INFO_KEY)
}

// 清除所有认证信息
export function clearAuth() {
  removeToken()
  removeUserInfo()
}
