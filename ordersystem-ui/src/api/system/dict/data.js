import { get, post, put, qsdeletefn } from "@/utils/request";

/**
 * 获取 dictdata 列表
 * @param { Object } data 分页信息
 * @returns {Promise<unknown>}
 */
export const listData = (data) => get('/system/dict/data/list', data);

/**
 * 通过 id 获取 dictdata
 * @param { Number } dictCode
 * @returns { Promise<unknown> }
 */
export const queryData = (dictCode) => get(`/system/dict/data/${dictCode}`);

/**
 * 根据字典类型查询字典数据信息
 * @param { String } dictType
 * @returns { Promise<unknown> }
 */
export const queryDicts = (dictType) => get(`/system/dict/data/type/${dictType}`);

/**
 * 通过 data 对象保存 dictdata, 不指定 id
 * @param { Object } data dictdata 对象
 * @returns {Promise<unknown>}
 */
export const addDict = (data) => post('/system/dict/data', data);

/**
 * 通过 data 对象修改 dictdata, 指定 id
 * @param { Object } data dictdata 对象
 * @returns {Promise<unknown>}
 */
export const editData = (data) => put('/system/dict/data', data);

/**
 * 通过 data 列表删除 dictdata
 * @param { Object } data = { dictCodes: [...]}
 * @returns {Promise<unknown>}
 */
export const removeData = (data) => qsdeletefn('/system/dict/data', data);
