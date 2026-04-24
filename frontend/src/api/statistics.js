import request from '@/utils/request'

/**
 * 按部门统计员工数量
 * @returns {Promise}
 */
export function getEmployeeByDept() {
  return request({
    url: '/api/statistics/employee-by-dept',
    method: 'get'
  })
}

/**
 * 获取考勤月报统计
 * @param {Object} params - 查询参数 { employeeId, month }
 * @returns {Promise}
 */
export function getAttendanceReport(params) {
  return request({
    url: '/api/statistics/attendance-report',
    method: 'get',
    params
  })
}

/**
 * 获取绩效排名统计
 * @param {Object} params - 查询参数 { evalDate }
 * @returns {Promise}
 */
export function getPerformanceRanking(params) {
  return request({
    url: '/api/statistics/performance-ranking',
    method: 'get',
    params
  })
}
