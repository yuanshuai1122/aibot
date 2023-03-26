// 登录
import request from '@/utils/request'
import api from '@/api/index'

export function chatSign(data) {
  return request({
    url: api.ChatSign,
    method: 'post',
    data
  })
}
