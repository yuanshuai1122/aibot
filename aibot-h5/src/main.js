// 兼容 IE
// https://github.com/zloirock/core-js/blob/master/docs/2019-03-19-core-js-3-babel-and-a-look-into-the-future.md#babelpolyfill
import 'core-js/stable'
import 'regenerator-runtime/runtime'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 设置 js中可以访问 $cdn
import { $cdn } from '@/config'
Vue.prototype.$cdn = $cdn

// 全局引入按需引入UI库 vant
import '@/plugins/vant'
// 引入全局样式
import '@/assets/css/index.scss'
// 移动端适配
import 'amfe-flexible'

import { Field,CellGroup,Grid, GridItem,Cell} from 'vant';
import { Image as VanImage } from 'vant';

// filters
import './filters'
Vue.config.productionTip = false


Vue.use(VanImage);
Vue.use(Field);
Vue.use(CellGroup);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(Cell);
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
