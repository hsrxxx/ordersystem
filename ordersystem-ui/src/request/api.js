/**   
 * api接口统一管理
 */
import { get, post, put, deletefn, qsget, qspost, qsput, qsdeletefn } from './http'

// ---------------------------------------------------------------------------------------------------------------------
// product.vue api

/**
 * 获取 product 列表
 * @param { data } 分页信息
 * @returns {Promise<unknown>}
 */
export const productList = (data) => get('/system/product/list', data);

/**
 * 通过 id 获取 product
 * @param { Number } id
 * @returns { Promise<unknown> }
 */
export const productQuery = (id) => get(`/system/product/${id}`);

/**
 * 获取 product type 列表
 * @returns {Promise<unknown>}
 */
export const typeList = () => get('/system/type/list');

/**
 * 通过 data 对象保存 product, id 为空
 * @param { Object } data = { id: ..., name: ..., price: ..., flavor: ..., tid: ... }
 * @returns {Promise<unknown>}
 */
export const productAdd = (data) => post('/system/product', data);

/**
 * 通过 data 对象修改 product, id 不为空
 * @param { Object } data = { id: ..., name: ..., price: ..., flavor: ..., tid: ... }
 * @returns {Promise<unknown>}
 */
export const productEdit = (data) => put('/system/product', data);

/**
 * 通过 data 列表删除 product
 * @param { Object } data = { ids: [...]}
 * @returns {Promise<unknown>}
 */
export const productRemove = (data) => qsdeletefn(`/system/product`, data);

// ---------------------------------------------------------------------------------------------------------------------
// user.vue api

/**
 * 分页获取 user 列表
 * @param { Number } index 页数
 * @param { Number } limit 每页展示数
 * @returns { Promise<unknown> }
 */
export const userList = (data) => qsget('/system/user/list', data);

/**
 * 通过 id 获取 user
 * @param { Number } id
 * @returns { Promise<unknown> }
 */
export const userQuery = (id) => get(`/system/user/${id}`);

/**
 * 通过 data 对象保存 user, id 为空
 * @param { Object } data = { id: ..., username: ..., nickname: ..., email: ..., telephone: ..., sex: ..., address: ..., remark: ..., status: ... }
 * @returns {Promise<unknown>}
 */
export const userAdd = (data) => post('/system/user', data);

/**
 * 通过 data 对象修改 user, id 不为空
 * @param { Object } data = { id: ..., username: ..., nickname: ..., email: ..., telephone: ..., sex: ..., address: ..., remark: ..., status: ... }
 * @returns {Promise<unknown>}
 */
export const userEdit = (data) => put('/system/user', data);

/**
 * 通过 data 列表删除 user
 * @param { Object } data = { ids: [...]}
 * @returns {Promise<unknown>}
 */
export const userRemove = (data) => qsdeletefn(`/system/user`, data);

/**
 * 通过 headers 内的 token 获取用户信息
 * @returns {Promise<unknown>}
 */
export const userGetInfo = () => get('/system/user/getInfo');

/**
 * 通过 data 对象修改 用户密码, id 不为空
 * @param { Object } data = { id: ..., username: ..., nickname: ..., email: ..., telephone: ..., sex: ..., address: ..., remark: ..., status: ... }
 * @returns {Promise<unknown>}
 */
export const resetPwd = (data) => put('/system/user/resetPwd', data);

/**
 * 通过 data 对象修改 用户状态, id 不为空
 * @param { Object } data = { id: ..., username: ..., nickname: ..., email: ..., telephone: ..., sex: ..., address: ..., remark: ..., status: ... }
 * @returns {Promise<unknown>}
 */
export const changeStatus = (data) => put('/system/user/changeStatus', data);

/**
 * 用户登陆
 * @param data
 * @returns {Promise<unknown>}
 */
export const login = (data) => qspost('/oauth/oauth/token', data);