import { get, qsget, post, put, deletefn } from "@/utils/request";

/**
 * 获取 menu 列表
 * @param { Object } data 菜单查询参数
 * @returns {Promise<unknown>}
 */
export const listMenu = (data) => qsget('/system/menu/list', data);

/**
 * 获取 menu 下拉树列表
 * @returns {Promise<unknown>}
 */
export const treeselect = () => get('/system/menu/treeselect');

/**
 * 通过角色 id 获取 menu
 * @param { Number } roleId
 * @returns { Promise<unknown> }
 */
export const roleMenuTreeselect = (roleId) => get(`/system/menu/roleMenuTreeselect/${roleId}`);

/**
 * 通过 id 获取 menu
 * @param { Number } menuId
 * @returns { Promise<unknown> }
 */
export const queryMenu = (menuId) => get(`/system/menu/${menuId}`);

/**
 * 通过 data 对象保存 menu, id 为空
 * @param { Object } data = menu 对象
 * @returns {Promise<unknown>}
 */
export const addMenu = (data) => post('/system/menu', data);

/**
 * 通过 data 对象修改 menu, id 不为空
 * @param { Object } data = menu 对象
 * @returns {Promise<unknown>}
 */
export const editMenu = (data) => put('/system/menu', data);

/**
 * 通过 Number id 删除 menu
 * @param { Number } id
 * @returns {Promise<unknown>}
 */
export const removeMenu = (id) => deletefn(`/system/menu/${id}`, );
