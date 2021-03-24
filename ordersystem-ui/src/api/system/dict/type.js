import { get, post, put, qsdeletefn, deletefn } from "@/utils/request";

/**
 * 获取 dicttype 列表
 * @param { Object } data 分页信息
 * @returns {Promise<unknown>}
 */
export const listType = (data) => get('/system/dict/type/list', data);

/**
 * 通过 id 获取 dicttype
 * @param { Number } dictId
 * @returns { Promise<unknown> }
 */
export const queryType = (dictId) => get(`/system/dict/type/${dictId}`);

/**
 * 通过 data 对象保存 dicttype, 不指定 id
 * @param { Object } data = dicttype 对象
 * @returns {Promise<unknown>}
 */
export const addType = (data) => post('/system/dict/type', data);

/**
 * 通过 data 对象修改 dicttype, 指定 id
 * @param { Object } data = dicttype 对象
 * @returns {Promise<unknown>}
 */
export const editType = (data) => put('/system/dict/type', data);

/**
 * 通过 data 列表删除 dicttype
 * @param { Object } data = { dictIds: [...]}
 * @returns {Promise<unknown>}
 */
export const removeType = (data) => qsdeletefn('/system/dict/type', data);

/**
 * 清理参数缓存
 * @returns {Promise<unknown>}
 */
export const clearCache = () => deletefn('/system/dict/type/clearCache');

/**
 * 获取字典选择框列表
 * @returns { Promise<unknown> }
 */
export const optionselect = () => get('/system/dict/type/optionselect');