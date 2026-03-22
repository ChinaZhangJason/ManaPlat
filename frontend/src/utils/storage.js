export const storage = {
  set(key, value, expire = null) {
    const data = {
      value,
      expire: expire ? Date.now() + expire : null
    }
    localStorage.setItem(key, JSON.stringify(data))
  },

  get(key) {
    const item = localStorage.getItem(key)
    if (!item) return null
    
    try {
      const data = JSON.parse(item)
      if (data.expire && Date.now() > data.expire) {
        localStorage.removeItem(key)
        return null
      }
      return data.value
    } catch {
      return null
    }
  },

  remove(key) {
    localStorage.removeItem(key)
  },

  clear() {
    localStorage.clear()
  },

  keys() {
    return Object.keys(localStorage)
  },

  has(key) {
    return localStorage.getItem(key) !== null
  }
}

export const sessionStorage = {
  set(key, value) {
    sessionStorage.setItem(key, JSON.stringify(value))
  },

  get(key) {
    const item = sessionStorage.getItem(key)
    if (!item) return null
    try {
      return JSON.parse(item)
    } catch {
      return null
    }
  },

  remove(key) {
    sessionStorage.removeItem(key)
  },

  clear() {
    sessionStorage.clear()
  }
}

export const cookie = {
  set(name, value, days = 7) {
    const expires = new Date()
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000)
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/`
  },

  get(name) {
    const nameEQ = name + '='
    const ca = document.cookie.split(';')
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i]
      while (c.charAt(0) === ' ') c = c.substring(1, c.length)
      if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length)
    }
    return null
  },

  remove(name) {
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`
  }
}
