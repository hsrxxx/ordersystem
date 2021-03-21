import { get } from "@/utils/request";

/**
 * 获取前端路由信息
 * @param data
 * @returns {Promise<unknown>}
 */
export const getRouters = () => get('/system/menu/getRouters');