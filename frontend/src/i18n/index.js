import { createI18n } from 'vue-i18n'
import zhCN from 'element-plus/dist/locale/zh-cn.mjs'

const messages = {
  'zh-CN': {
    ...zhCN,
    common: {
      save: '保存',
      cancel: '取消',
      delete: '删除',
      edit: '编辑',
      add: '新增',
      search: '搜索',
      reset: '重置',
      submit: '提交',
      confirm: '确认',
      success: '操作成功',
      error: '操作失败',
      warning: '警告',
      info: '提示',
      loading: '加载中...',
      noData: '暂无数据'
    },
    menu: {
      dashboard: '仪表盘',
      monitoring: '监控管理',
      system: '系统管理',
      application: '应用管理',
      integration: '系统集成',
      test: '测试工具'
    },
    auth: {
      login: '登录',
      logout: '退出登录',
      username: '用户名',
      password: '密码',
      forgotPassword: '忘记密码?',
      resetPassword: '重置密码',
      sendCode: '发送验证码',
      newPassword: '新密码',
      confirmPassword: '确认密码'
    },
    alert: {
      rule: '告警规则',
      receiver: '接收人',
      history: '告警历史',
      level: '告警级别',
      status: '状态',
      pending: '待处理',
      sent: '已发送',
      acked: '已确认',
      closed: '已关闭'
    },
    report: {
      title: '报表配置',
      widget: '报表组件',
      dataSource: '数据源',
      chartType: '图表类型'
    },
    sap: {
      rfc: 'RFC配置',
      api: 'API配置',
      webservice: 'WebService配置',
      hana: 'HANA连接',
      testConnection: '测试连接'
    }
  },
  'en': {
    common: {
      save: 'Save',
      cancel: 'Cancel',
      delete: 'Delete',
      edit: 'Edit',
      add: 'Add',
      search: 'Search',
      reset: 'Reset',
      submit: 'Submit',
      confirm: 'Confirm',
      success: 'Success',
      error: 'Error',
      warning: 'Warning',
      info: 'Info',
      loading: 'Loading...',
      noData: 'No Data'
    },
    menu: {
      dashboard: 'Dashboard',
      monitoring: 'Monitoring',
      system: 'System',
      application: 'Application',
      integration: 'Integration',
      test: 'Test Tools'
    }
  }
}

export const i18n = createI18n({
  legacy: false,
  locale: 'zh-CN',
  fallbackLocale: 'zh-CN',
  messages
})

export default i18n
