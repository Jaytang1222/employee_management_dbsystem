import request from '@/utils/request'

/**
 * 获取员工列表（分页+搜索）
 * @param {Object} params - 查询参数 { page, pageSize, name, deptId, positionId, status }
 * @returns {Promise}
 */
export function getEmployeeList(params) {
  return request({
    url: '/api/employees',
    method: 'get',
    params
  })
}

/**
 * 获取员工详情
 * @param {Number} id - 员工ID
 * @returns {Promise}
 */
export function getEmployeeById(id) {
  return request({
    url: `/api/employees/${id}`,
    method: 'get'
  })
}

/**
 * 新增员工
 * @param {Object} data - 员工信息
 * @returns {Promise}
 */
export function addEmployee(data) {
  return request({
    url: '/api/employees',
    method: 'post',
    data
  })
}

/**
 * 更新员工
 * @param {Number} id - 员工ID
 * @param {Object} data - 员工信息
 * @returns {Promise}
 */
export function updateEmployee(id, data) {
  return request({
    url: `/api/employees/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除员工（软删除）
 * @param {Number} id - 员工ID
 * @returns {Promise}
 */
export function deleteEmployee(id) {
  return request({
    url: `/api/employees/${id}`,
    method: 'delete'
  })
}
