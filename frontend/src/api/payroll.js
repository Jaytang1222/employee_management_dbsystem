import request from '@/utils/request'

/**
 * 获取薪资列表（分页+搜索）
 * @param {Object} params - 查询参数 { page, pageSize, employeeId, payMonth }
 * @returns {Promise}
 */
export function getPayrollList(params) {
  return request({
    url: '/api/payrolls',
    method: 'get',
    params
  })
}

/**
 * 获取薪资详情
 * @param {Number} id - 薪资ID
 * @returns {Promise}
 */
export function getPayrollById(id) {
  return request({
    url: `/api/payrolls/${id}`,
    method: 'get'
  })
}

/**
 * 新增薪资记录
 * @param {Object} data - 薪资信息
 * @returns {Promise}
 */
export function addPayroll(data) {
  return request({
    url: '/api/payrolls',
    method: 'post',
    data
  })
}

/**
 * 更新薪资记录
 * @param {Number} id - 薪资ID
 * @param {Object} data - 薪资信息
 * @returns {Promise}
 */
export function updatePayroll(id, data) {
  return request({
    url: `/api/payrolls/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除薪资记录
 * @param {Number} id - 薪资ID
 * @returns {Promise}
 */
export function deletePayroll(id) {
  return request({
    url: `/api/payrolls/${id}`,
    method: 'delete'
  })
}
