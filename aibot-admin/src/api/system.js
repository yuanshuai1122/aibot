
import request from '@/utils/request'


// 获取key列表
export function getKeyList() {
  return request({
    url: '/system/key/list',
    method: 'get'
  })
}

// 查询key信息
export function getKeyInfo(params) {
  return request({
    url: '/system/billing/check',
    method: 'get',
    params: params
  })
}

// 更新key状态
export function updateKeyStatus(data) {
  return request({
    url: '/system/key/status',
    method: 'post',
    data: data
  })
}
