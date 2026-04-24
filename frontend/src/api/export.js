import request from '@/utils/request'
import fileDownload from 'js-file-download'

/**
 * 导出员工列表
 * @param {Object} params - 查询参数（与列表查询参数相同）
 * @returns {Promise}
 */
export function exportEmployees(params) {
  return request({
    url: '/api/export/employees',
    method: 'get',
    params,
    responseType: 'blob'
  }).then(response => {
    const fileName = `employees_${new Date().getTime()}.xlsx`
    fileDownload(response, fileName)
  })
}

/**
 * 导出考勤记录
 * @param {Object} params - 查询参数 { month }
 * @returns {Promise}
 */
export function exportAttendances(params) {
  return request({
    url: '/api/export/attendances',
    method: 'get',
    params,
    responseType: 'blob'
  }).then(response => {
    const fileName = `attendances_${params.month || new Date().getTime()}.xlsx`
    fileDownload(response, fileName)
  })
}

/**
 * 导出薪资记录
 * @param {Object} params - 查询参数 { month }
 * @returns {Promise}
 */
export function exportPayrolls(params) {
  return request({
    url: '/api/export/payrolls',
    method: 'get',
    params,
    responseType: 'blob'
  }).then(response => {
    const fileName = `payrolls_${params.month || new Date().getTime()}.xlsx`
    fileDownload(response, fileName)
  })
}

/**
 * 导出绩效排名
 * @param {Object} params - 查询参数 { evalDate }
 * @returns {Promise}
 */
export function exportPerformanceRanking(params) {
  return request({
    url: '/api/export/performance-ranking',
    method: 'get',
    params,
    responseType: 'blob'
  }).then(response => {
    const fileName = `performance_ranking_${params.evalDate || new Date().getTime()}.xlsx`
    fileDownload(response, fileName)
  })
}
