import request from '@/utils/request'

/**
 * 获取岗位列表（分页）
 * @param {Object} params - 查询参数 { pageNum, pageSize, positionName, positionLevel, status }
 * @returns {Promise}
 */
export function getPositionList(params) {
  return request({
    url: '/api/positions',
    method: 'get',
    params
  })
}

/**
 * 获取所有岗位（不分页，用于下拉选择）
 * @returns {Promise}
 */
export function getAllPositions() {
  return request({
    url: '/api/positions/all',
    method: 'get'
  })
}

/**
 * 获取岗位详情
 * @param {Number} id - 岗位ID
 * @returns {Promise}
 */
export function getPositionById(id) {
  return request({
    url: `/api/positions/${id}`,
    method: 'get'
  })
}

/**
 * 新增岗位
 * @param {Object} data - 岗位信息
 * @returns {Promise}
 */
export function addPosition(data) {
  return request({
    url: '/api/positions',
    method: 'post',
    data
  })
}

/**
 * 更新岗位
 * @param {Number} id - 岗位ID
 * @param {Object} data - 岗位信息
 * @returns {Promise}
 */
export function updatePosition(id, data) {
  return request({
    url: `/api/positions/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除岗位
 * @param {Number} id - 岗位ID
 * @returns {Promise}
 */
export function deletePosition(id) {
  return request({
    url: `/api/positions/${id}`,
    method: 'delete'
  })
}
