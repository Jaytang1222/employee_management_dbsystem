import request from '@/utils/request'

/**
 * 获取部门列表
 * @returns {Promise}
 */
export function getDepartmentList() {
  return request({
    url: '/api/departments',
    method: 'get'
  })
}

/**
 * 获取部门树形结构
 * @returns {Promise}
 */
export function getDepartmentTree() {
  return request({
    url: '/api/departments/tree',
    method: 'get'
  })
}

/**
 * 获取部门详情
 * @param {Number} id - 部门ID
 * @returns {Promise}
 */
export function getDepartmentById(id) {
  return request({
    url: `/api/departments/${id}`,
    method: 'get'
  })
}

/**
 * 新增部门
 * @param {Object} data - 部门信息
 * @returns {Promise}
 */
export function addDepartment(data) {
  return request({
    url: '/api/departments',
    method: 'post',
    data
  })
}

/**
 * 更新部门
 * @param {Number} id - 部门ID
 * @param {Object} data - 部门信息
 * @returns {Promise}
 */
export function updateDepartment(id, data) {
  return request({
    url: `/api/departments/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除部门
 * @param {Number} id - 部门ID
 * @returns {Promise}
 */
export function deleteDepartment(id) {
  return request({
    url: `/api/departments/${id}`,
    method: 'delete'
  })
}
