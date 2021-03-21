import { get, post, put, qsdeletefn } from "@/utils/request";

/**
 * 获取 menu 列表
 * @returns {Promise<unknown>}
 */
export const listMenu = () => get('/system/menu/list');