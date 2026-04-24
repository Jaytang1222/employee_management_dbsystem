import request from '@/utils/request'

/**
 * 获取员工分配列表（分页）
 * @param {Object} params - 查询参数 { page, pageSize, employeeId, deptId, positionId }
 * @returns {Promise}
 */
export function getAssignmentList(params) {
  return request({
    url: '/api/assignments',
    method: 'get',
    params
  })
}

/**
 * 获取员工分配详情
 * @param {Number} id - 分配ID
 * @returns {Promise}
 */
export function getAssignmentById(id) {
  return request({
    url: `/api/assignments/${id}`,
    method: 'get'
  })
}

/**
 * 新增员工分配
 * @param {Object} data - 分配信息
 * @returns {Promise}
 */
export function addAssignment(data) {
  return request({
    url: '/api/assignments',
    method: 'post',
    data
  })
}

/**
 * 更新员工分配
 * @param {Number} id - 分配ID
 * @param {Object} data - 分配信息
 * @returns {Promise}
 */
export function updateAssignment(id, data) {
  return request({
    url: `/api/assignments/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除员工分配
 * @param {Number} id - 分配ID
 * @returns {Promise}
 */
export function deleteAssignment(id) {
  return request({
    url: `/api/assignments/${id}`,
    method: 'delete'
  })
}
