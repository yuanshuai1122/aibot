import request from '@/utils/request'

export function productListApi(pageNum,pageSize,productName) {
  return request({
    url: 'product/list?pageNum='+pageNum+'&pageSize='+pageSize+'&productName='+productName,
    method: 'get',
  })
}
