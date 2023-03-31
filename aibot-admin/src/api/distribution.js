import request from '@/utils/request'

// 获取分销列表
export function getDistriList(params) {
  return request({
    url: '/distribution/list',
    method: 'get',
    params
  })
}
//获取最大分销层级
export function getMaxLevel(params) {
  return request({
    url: '/distribution/level/max',
    method: 'get',
    params
  })
}
//获取详情
export function getInfoApi(id) {
  return request({
    url: '/distribution/config?id='+id,
    method: 'get',
  })
}
//修改分销
export function updateApi(data) {
  return request({
    url: '/distribution/config/update',
    method: 'post',
    data:data
  })
}
