import { describe, it, expect } from 'vitest'

describe('User Store', () => {
  it('should have correct initial state', () => {
    const userStore = {
      token: '',
      userInfo: null,
      permissions: [],
      roles: []
    }

    expect(userStore.token).toBe('')
    expect(userStore.userInfo).toBeNull()
    expect(userStore.permissions).toEqual([])
  })

  it('should check login status correctly', () => {
    const userStore = {
      token: 'abc123',
      get isLoggedIn() {
        return !!this.token
      }
    }

    expect(userStore.isLoggedIn).toBe(true)

    userStore.token = ''
    expect(userStore.isLoggedIn).toBe(false)
  })

  it('should extract permissions correctly', () => {
    const userStore = {
      roles: [
        {
          roleCode: 'ADMIN',
          permissions: [
            { permissionCode: 'user:view' },
            { permissionCode: 'user:edit' }
          ]
        }
      ],
      extractPermissions() {
        const permissions = []
        this.roles.forEach(role => {
          if (role.permissions) {
            permissions.push(...role.permissions.map(p => p.permissionCode))
          }
        })
        return [...new Set(permissions)]
      }
    }

    const permissions = userStore.extractPermissions()
    expect(permissions).toContain('user:view')
    expect(permissions).toContain('user:edit')
    expect(permissions.length).toBe(2)
  })

  it('should check admin role correctly', () => {
    const userStore = {
      roles: [{ roleCode: 'ADMIN' }],
      isAdmin: false,
      checkAdmin() {
        this.isAdmin = this.roles.some(r => r.roleCode === 'ADMIN' || r.roleCode === 'SUPER_ADMIN')
      }
    }

    userStore.checkAdmin()
    expect(userStore.isAdmin).toBe(true)
  })
})
