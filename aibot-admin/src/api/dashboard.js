import request from '@/utils/request'

// 获取分销列表
export function getDataList(params) {
  return request({
    url: '/statistics/list',
    method: 'get',
    params
  })
}

// 获取登录信息
export function getLoginInfo() {
  return request({
    url: '/user/login/info',
    method: 'get'
  })
}
