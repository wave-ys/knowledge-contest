const TokenKey = "Authorization";

export function setSessionStorage(key, value) {
  window.sessionStorage.setItem(key, window.JSON.stringify(value));
}

export function getSessionStorage(key) {
  return window.JSON.parse(window.sessionStorage.getItem(key) || "[]");
}

export function removeSessionStorage(key) {
  window.sessionStorage.removeItem(key);
}

export function getToken() {
  return getSessionStorage(TokenKey);
}

export function setToken(token) {
  return setSessionStorage(TokenKey, token);
}

export function removeToken() {
  return removeSessionStorage(TokenKey);
}
