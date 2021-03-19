import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message } from 'element-ui';
import Layout from '@/layout/index'
import Product from '../views/system/product/index'
import User from '../views/system/user/index'

Vue.use(VueRouter)

export const constantRoutes = [
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
  },
  {
    path: '/register',
    name: '用户注册',
    component: (resolve) => require(['@/views/register'], resolve),
  },
  {
    path: '',
    name: '系统管理',
    component: Layout,
    icon: 'el-icon-s-promotion',
    show: true,
    children: [
      {
        path: '/product',
        name: '菜单管理',
        component: Product,
      },
      {
        path: '/user',
        name: '用户管理',
        component: User,
      }
    ]
  },
]

const router = new VueRouter({
  // history 模式下刷新404
  // mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

router.beforeEach((to, from, next) => {
  //to到哪儿 from从哪儿离开 next跳转 为空就是放行
  if (to.path === '/login') {
    //如果跳转为登录，就放行
    next();
  } else {
    //取出localStorage判断
    let token = localStorage.getItem('token');
    if (token == null || token === '') {
      Message.error('请先登录用户')
      next({path: 'login'});
    } else {
      next();
    }
  }
  });

export default router
