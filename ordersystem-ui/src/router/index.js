import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout/index'
import Product from '../views/system/product/index'
import User from '../views/system/user/index'

Vue.use(VueRouter)

export const constantRoutes = [
  {
    path: '/login',
    hidden: true,
    component: (resolve) => require(['@/views/login'], resolve),
  },
  {
    path: '/register',
    hidden: true,
    component: (resolve) => require(['@/views/register'], resolve),
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'el-icon-s-home', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
]

const router = new VueRouter({
  // history 模式下刷新404
  // mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

export default router
