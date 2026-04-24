export default {
  // 通用
  common: {
    login: '登录',
    logout: '登出',
    save: '保存',
    cancel: '取消',
    delete: '删除',
    edit: '编辑',
    add: '新增',
    search: '搜索',
    reset: '重置',
    export: '导出',
    confirm: '确认',
    operation: '操作',
    status: '状态',
    createTime: '创建时间',
    updateTime: '更新时间',
    action: '操作',
    detail: '详情',
    back: '返回',
    submit: '提交',
    close: '关闭',
    view: '查看',
    selectAll: '全选',
    batchDelete: '批量删除',
    refresh: '刷新',
    loading: '加载中...',
    noData: '暂无数据',
    total: '共',
    items: '条',
    success: '成功',
    failed: '失败',
    pleaseSelect: '请选择',
    pleaseInput: '请输入',
    confirmDelete: '确认删除吗？',
    deleteSuccess: '删除成功',
    saveSuccess: '保存成功',
    operationSuccess: '操作成功',
    operationFailed: '操作失败',
    tips: '提示',
    warning: '警告'
  },

  // 菜单
  menu: {
    home: '首页',
    employee: '员工管理',
    department: '部门管理',
    position: '岗位管理',
    assignment: '员工分配',
    attendance: '考勤管理',
    payroll: '薪资管理',
    performance: '绩效管理',
    statistics: '统计报表'
  },

  // 登录
  login: {
    title: '企业员工管理系统',
    username: '用户名',
    password: '密码',
    rememberMe: '记住密码',
    loginBtn: '登录',
    usernamePlaceholder: '请输入用户名',
    passwordPlaceholder: '请输入密码',
    usernameRequired: '请输入用户名',
    passwordRequired: '请输入密码',
    loginSuccess: '登录成功',
    loginFailed: '登录失败'
  },

  // 员工
  employee: {
    name: '姓名',
    idCard: '身份证号',
    gender: '性别',
    birthDate: '出生日期',
    phone: '手机号',
    email: '邮箱',
    address: '地址',
    hireDate: '入职日期',
    educationLevel: '学历',
    emergencyContact: '紧急联系人',
    male: '男',
    female: '女',
    highSchool: '高中',
    college: '大专',
    bachelor: '本科',
    master: '硕士',
    doctor: '博士',
    active: '在职',
    inactive: '离职',
    deleted: '已删除',
    addEmployee: '新增员工',
    editEmployee: '编辑员工',
    employeeDetail: '员工详情',
    employeeList: '员工列表',
    idCardFormat: '请输入正确的身份证号',
    phoneFormat: '请输入正确的手机号',
    emailFormat: '请输入正确的邮箱'
  },

  // 部门
  department: {
    name: '部门名称',
    parentDept: '上级部门',
    manager: '部门经理',
    addDepartment: '新增部门',
    editDepartment: '编辑部门',
    departmentTree: '部门树',
    rootDepartment: '根部门',
    noDepartment: '无上级部门'
  },

  // 岗位
  position: {
    name: '岗位名称',
    level: '岗位级别',
    baseSalary: '基本工资',
    description: '岗位描述',
    junior: '初级',
    intermediate: '中级',
    senior: '高级',
    expert: '专家',
    addPosition: '新增岗位',
    editPosition: '编辑岗位',
    positionList: '岗位列表'
  },

  // 员工分配
  assignment: {
    employee: '员工',
    department: '部门',
    position: '岗位',
    startDate: '生效日期',
    isPrimary: '是否主部门',
    changeReason: '调动原因',
    yes: '是',
    no: '否',
    addAssignment: '新增分配',
    editAssignment: '编辑分配',
    assignmentList: '分配列表'
  },

  // 考勤
  attendance: {
    employee: '员工',
    date: '考勤日期',
    checkInTime: '签到时间',
    checkOutTime: '签退时间',
    status: '考勤状态',
    lateMinutes: '迟到分钟',
    earlyLeaveMinutes: '早退分钟',
    overtimeHours: '加班小时',
    remark: '备注',
    normal: '正常',
    late: '迟到',
    earlyLeave: '早退',
    absent: '缺勤',
    leave: '请假',
    addAttendance: '新增考勤',
    editAttendance: '编辑考勤',
    attendanceList: '考勤列表',
    monthlyReport: '考勤月报',
    selectMonth: '选择月份',
    attendanceDays: '出勤天数',
    lateTimes: '迟到次数',
    earlyLeaveTimes: '早退次数',
    totalOvertimeHours: '加班总时长',
    absentDays: '缺勤天数'
  },

  // 薪资
  payroll: {
    employee: '员工',
    month: '发薪月份',
    basePay: '基本工资',
    allowance: '津贴',
    bonus: '奖金',
    deduction: '扣款',
    socialInsurance: '社保',
    tax: '税',
    netPay: '实发工资',
    payDate: '发薪日期',
    addPayroll: '新增薪资',
    editPayroll: '编辑薪资',
    payrollList: '薪资列表',
    calculate: '计算实发工资'
  },

  // 绩效
  performance: {
    employee: '员工',
    evalDate: '考核周期',
    score: '绩效分数',
    grade: '绩效等级',
    comment: '评价意见',
    evaluateDate: '评估日期',
    gradeA: '优秀',
    gradeB: '良好',
    gradeC: '合格',
    gradeD: '待改进',
    addPerformance: '新增绩效',
    editPerformance: '编辑绩效',
    performanceList: '绩效列表',
    ranking: '绩效排名',
    rank: '排名'
  },

  // 统计报表
  statistics: {
    title: '统计报表',
    employeeStats: '员工统计',
    attendanceStats: '考勤统计',
    performanceStats: '绩效统计',
    employeeByDept: '按部门统计员工',
    attendanceReport: '考勤月报',
    performanceRanking: '绩效排名',
    departmentName: '部门名称',
    employeeCount: '员工数量',
    totalEmployees: '员工总数',
    totalDepartments: '部门总数',
    todayAttendance: '今日考勤'
  },

  // 分页
  pagination: {
    total: '共',
    items: '条',
    pageSize: '每页',
    goto: '跳至',
    page: '页'
  }
}
