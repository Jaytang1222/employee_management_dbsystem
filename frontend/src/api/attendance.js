import request from '@/utils/request'

/**
 * 获取考勤列表（分页+搜索）
 * @param {Object} params - 查询参数 { page, pageSize, employeeId, startDate, endDate, status }
 * @returns {Promise}
 */
export function getAttendanceList(params) {
  return request({
    url: '/api/attendances',
    method: 'get',
    params
  })
}

/**
 * 获取考勤详情
 * @param {Number} id - 考勤ID
 * @returns {Promise}
 */
export function getAttendanceById(id) {
  return request({
    url: `/api/attendances/${id}`,
    method: 'get'
  })
}

/**
 * 新增考勤记录
 * @param {Object} data - 考勤信息
 * @returns {Promise}
 */
export function addAttendance(data) {
  return request({
    url: '/api/attendances',
    method: 'post',
    data
  })
}

/**
 * 更新考勤记录
 * @param {Number} id - 考勤ID
 * @param {Object} data - 考勤信息
 * @returns {Promise}
 */
export function updateAttendance(id, data) {
  return request({
    url: `/api/attendances/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除考勤记录
 * @param {Number} id - 考勤ID
 * @returns {Promise}
 */
export function deleteAttendance(id) {
  return request({
    url: `/api/attendances/${id}`,
    method: 'delete'
  })
}

/**
 * 获取考勤月报
 * @param {Object} params - 查询参数 { employeeId, month }
 * @returns {Promise}
 */
export function getAttendanceReport(params) {
  return request({
    url: '/api/attendances/report',
    method: 'get',
    params
  })
}
