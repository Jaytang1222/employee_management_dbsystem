import request from '@/utils/request'

/**
 * 获取绩效列表（分页+搜索）
 * @param {Object} params - 查询参数 { page, pageSize, employeeId, startDate, endDate, grade }
 * @returns {Promise}
 */
export function getPerformanceList(params) {
  return request({
    url: '/api/performances',
    method: 'get',
    params
  })
}

/**
 * 获取绩效详情
 * @param {Number} id - 绩效ID
 * @returns {Promise}
 */
export function getPerformanceById(id) {
  return request({
    url: `/api/performances/${id}`,
    method: 'get'
  })
}

/**
 * 新增绩效记录
 * @param {Object} data - 绩效信息
 * @returns {Promise}
 */
export function addPerformance(data) {
  return request({
    url: '/api/performances',
    method: 'post',
    data
  })
}

/**
 * 更新绩效记录
 * @param {Number} id - 绩效ID
 * @param {Object} data - 绩效信息
 * @returns {Promise}
 */
export function updatePerformance(id, data) {
  return request({
    url: `/api/performances/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除绩效记录
 * @param {Number} id - 绩效ID
 * @returns {Promise}
 */
export function deletePerformance(id) {
  return request({
    url: `/api/performances/${id}`,
    method: 'delete'
  })
}

/**
 * 获取绩效排名
 * @param {Object} params - 查询参数 { evalDate }
 * @returns {Promise}
 */
export function getPerformanceRanking(params) {
  return request({
    url: '/api/performances/ranking',
    method: 'get',
    params
  })
}
