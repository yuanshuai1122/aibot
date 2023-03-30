import request from '@/utils/request'

//订单列表
export function orderListApi(pageNum,pageSize,productName,nickName,orderStatus) {
  return request({
    url: '/order/list?pageNum='+pageNum+'&pageSize='+pageSize+'&productName='+productName+'&nickName='+nickName+'&orderStatus='+orderStatus,
    method: 'get',
  })
}
//订单状态列表
export function orderStatusApi() {
  return request({
    url: '/order/status/list',
    method: 'get',
  })
}
