import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken, removeToken } from '@/utils/auth'
import el from 'element-ui/src/locale/lang/el'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    config.headers['authorization'] = getToken()
    // if (store.getters.token) {
    //   // let each request carry token
    //   // ['X-Token'] is a custom headers key
    //   // please modify it according to the actual situation
    //
    // }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    console.log(response)
    if (response.data.code == 401) {
      console.log("登录过期，删除本地缓存")
      removeToken()
      // 提示语
      Message({
        message: "登录已失效，请刷新页面",
        type: 'error',
        duration: 5 * 1000
      })
    }

    return Promise.resolve(response.data)
  },
  error => {

    console.log('err' + error) // for debug
    return Promise.reject(error)
  }
)

export default service
