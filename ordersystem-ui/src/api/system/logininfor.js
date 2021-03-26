import { qsget, qsdeletefn, deletefn } from "@/utils/request";

/**
 * 获取 logininfor 列表
 * @param { Object } data 分页信息
 * @returns {Promise<unknown>}
 */
export const listLogininfor = (data) => qsget('/system/logininfor/list', data);

/**
 * 通过 data 列表删除 logininfor
 * @param { Object } data = { inforIds: [...]}
 * @returns {Promise<unknown>}
 */
export const removeLogininfor = (data) => qsdeletefn('/system/logininfor', data);

/**
 * 清空操作日志信息
 * @returns {Promise<unknown>}
 */
export const cleanLogininfor = () => deletefn('/system/logininfor/clean');