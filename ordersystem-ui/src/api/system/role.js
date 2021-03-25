import { get, qsget, post, put, qsdeletefn } from "@/utils/request";

/**
 * 分页获取 role 列表
 * @param { Object } data 分页信息
 * @returns { Promise<unknown> }
 */
export const listRole = (data) => qsget('/system/role/list', data);

/**
 * 通过 id 获取 role
 * @param { Number } id
 * @returns { Promise<unknown> }
 */
export const queryRole = (id) => get(`/system/role/${id}`);

/**
 * 通过 data 对象保存 role, id 为空
 * @param { Object } data = role 对象
 * @returns {Promise<unknown>}
 */
export const addRole = (data) => post('/system/role', data);

/**
 * 通过 data 对象修改 role, id 不为空
 * @param { Object } data = role 对象
 * @returns {Promise<unknown>}
 */
export const editRole = (data) => put('/system/role', data);

/**
 * 通过 data 列表删除 role
 * @param { Object } data = { roleIds: [...]}
 * @returns {Promise<unknown>}
 */
export const removeRole = (data) => qsdeletefn(`/system/role`, data);

/**
 * 通过 data 对象修改 用户状态, id 不为空
 * @param { Object } data = role 状态信息
 * @returns {Promise<unknown>}
 */
export const changeStatus = (data) => put('/system/role/changeStatus', data);
