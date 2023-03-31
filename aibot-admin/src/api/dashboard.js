import request from '@/utils/request'

// 获取分销列表
export function getDataList(params) {
  return request({
    url: '/statistics/list',
    method: 'get',
    params
  })
}
