import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import { Message } from 'element-ui';
import { resetForm, handleTree, selectDictLabel, parseTime, addDateRange } from "@/utils/common";
import { queryDicts } from '@/api/system/dict/data'
import 'element-ui/lib/theme-chalk/index.css';
import './permission' // permission control
// 自定义表格工具扩展
import RightToolbar from "@/components/RightToolbar"

// 图标样式
import iconPicker from 'e-icon-picker';
import "e-icon-picker/dist/symbol.js"; //基本彩色图标库
import 'e-icon-picker/dist/index.css'; // 基本样式，包含基本图标
import 'font-awesome/css/font-awesome.min.css'; //font-awesome 图标库

Vue.use(iconPicker, {FontAwesome: true, eIcon: true, eIconSymbol: true});

Vue.use(ElementUI);

Vue.prototype.Message = Message
Vue.prototype.getDicts = queryDicts
Vue.prototype.resetForm = resetForm
Vue.prototype.parseTime = parseTime
Vue.prototype.handleTree = handleTree
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel

Vue.config.productionTip = false

Vue.component('RightToolbar', RightToolbar)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
