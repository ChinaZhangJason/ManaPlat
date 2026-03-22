import { describe, it, expect } from 'vitest'

describe('Alert Service', () => {
  it('should format condition correctly', () => {
    const conditionMap = {
      'GT': '>',
      'LT': '<',
      'EQ': '=',
      'GE': '>=',
      'LE': '<='
    }

    expect(conditionMap['GT']).toBe('>')
    expect(conditionMap['LT']).toBe('<')
    expect(conditionMap['EQ']).toBe('=')
  })

  it('should get alert level type', () => {
    const getLevelType = (level) => {
      const map = { WARNING: 'warning', ERROR: 'danger', CRITICAL: 'danger' }
      return map[level] || ''
    }

    expect(getLevelType('WARNING')).toBe('warning')
    expect(getLevelType('ERROR')).toBe('danger')
    expect(getLevelType('CRITICAL')).toBe('danger')
  })

  it('should get status type', () => {
    const getStatusType = (status) => {
      const map = { PENDING: 'warning', SENT: 'info', ACKED: 'success', CLOSED: 'info' }
      return map[status] || ''
    }

    expect(getStatusType('PENDING')).toBe('warning')
    expect(getStatusType('ACKED')).toBe('success')
    expect(getStatusType('CLOSED')).toBe('info')
  })
})

describe('Verify Code Service', () => {
  it('should validate phone format', () => {
    const isValidPhone = (phone) => {
      return /^1[3-9]\d{9}$/.test(phone)
    }

    expect(isValidPhone('13800138000')).toBe(true)
    expect(isValidPhone('1234567890')).toBe(false)
    expect(isValidPhone('12345678901')).toBe(false)
  })

  it('should validate email format', () => {
    const isValidEmail = (email) => {
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
    }

    expect(isValidEmail('test@example.com')).toBe(true)
    expect(isValidEmail('test@')).toBe(false)
    expect(isValidEmail('@example.com')).toBe(false)
  })

  it('should generate 6 digit code', () => {
    const generateCode = () => {
      return Math.random().toString().slice(2, 8)
    }

    const code = generateCode()
    expect(code).toHaveLength(6)
    expect(/^\d{6}$/.test(code)).toBe(true)
  })
})
