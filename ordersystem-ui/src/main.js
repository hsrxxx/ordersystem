import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import { Message } from 'element-ui';
import { resetForm, handleTree } from "@/utils/common";
import { queryDicts } from '@/api/system/dict/data'
import 'element-ui/lib/theme-chalk/index.css';
import './permission' // permission control
// 自定义表格工具扩展
import RightToolbar from "@/components/RightToolbar"

Vue.use(ElementUI);

Vue.prototype.getDicts = queryDicts
Vue.prototype.resetForm = resetForm
Vue.prototype.handleTree = handleTree
Vue.prototype.$Message = Message

Vue.config.productionTip = false

Vue.component('RightToolbar', RightToolbar)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
