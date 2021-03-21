import { deletefn, post, qspost, get } from "../utils/request";

/**
 * 用户登陆
 * @param data
 * @returns {Promise<unknown>}
 */
export const login = (data) => qspost('/oauth/oauth/token', data);

/**
 * 通过 headers 内的 token 获取用户详情信息
 * @returns {Promise<unknown>}
 */
export const getInfo = () => get('/system/user/getInfo');

/**
 * 通过 headers 内的 token 刷新 token 时间
 * @returns {Promise<unknown>}
 */
export const refreshToken = () => post('/oauth/refreshToken');

/**
 * 通过 headers 内的 token 刷新 token 时间
 * @returns {Promise<unknown>}
 */
export const logout = () => deletefn('/oauth/logout');