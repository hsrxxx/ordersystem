import { qsget, qsdeletefn, deletefn } from "@/utils/request";

/**
 * 获取 operlog 列表
 * @param { Object } data 分页信息
 * @returns {Promise<unknown>}
 */
export const listOperlog = (data) => qsget('/system/operlog/list', data);

/**
 * 通过 data 列表删除 operlog
 * @param { Object } data = { operIds: [...]}
 * @returns {Promise<unknown>}
 */
export const removeOperlog = (data) => qsdeletefn('/system/operlog', data);

/**
 * 清空操作日志信息
 * @returns {Promise<unknown>}
 */
export const cleanOperlog = () => deletefn('/system/operlog/clean');