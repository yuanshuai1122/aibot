import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: '/user/info',
    method: 'get',
    params
  })
}
