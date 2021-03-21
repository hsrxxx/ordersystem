import { get, post, put, qsdeletefn, qsget } from "@/utils/request";

/**
 * 分页获取 user 列表
 * @param { Object } data 分页信息
 * @returns { Promise<unknown> }
 */
export const listUser = (data) => qsget('/system/user/list', data);

/**
 * 通过 id 获取 user
 * @param { Number } id
 * @returns { Promise<unknown> }
 */
export const queryUser = (id) => get(`/system/user/${id}`);

/**
 * 获取角色信息
 * @returns { Promise<unknown> }
 */
export const getRoles = () => get('/system/user/');

/**
 * 通过 data 对象保存 user, id 为空
 * @param { Object } data = user 对象
 * @returns {Promise<unknown>}
 */
export const addUser = (data) => post('/system/user', data);

/**
 * 通过 data 对象修改 user, id 不为空
 * @param { Object } data = user 对象
 * @returns {Promise<unknown>}
 */
export const editUser = (data) => put('/system/user', data);

/**
 * 通过 data 列表删除 user
 * @param { Object } data = { userIds: [...]}
 * @returns {Promise<unknown>}
 */
export const removeUser = (data) => qsdeletefn(`/system/user`, data);

/**
 * 通过 data 对象修改 用户密码, id 不为空
 * @param { Object } data = user 密码信息
 * @returns {Promise<unknown>}
 */
export const resetPwd = (data) => put('/system/user/resetPwd', data);

/**
 * 通过 data 对象修改 用户状态, id 不为空
 * @param { Object } data = user 状态信息
 * @returns {Promise<unknown>}
 */
export const changeStatus = (data) => put('/system/user/changeStatus', data);
