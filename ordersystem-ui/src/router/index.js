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
]

const router = new VueRouter({
  // history 模式下刷新404
  // mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

// router.beforeEach((to, from, next) => {
//   //to到哪儿 from从哪儿离开 next跳转 为空就是放行
//   if (to.path === '/login') {
//     //如果跳转为登录，就放行
//     next();
//   } else {
//     //取出localStorage判断
//     let token = localStorage.getItem('token');
//     if (token == null || token === '') {
//       Message.error('请先登录用户')
//       next({path: 'login'});
//     } else {
//       next();
//     }
//   }
//   });

export default router
