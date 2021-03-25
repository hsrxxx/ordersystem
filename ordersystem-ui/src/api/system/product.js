import { get, qsget, post, put, qsdeletefn } from "@/utils/request";

/**
 * 获取 product 列表
 * @param { Object } data 分页信息
 * @returns {Promise<unknown>}
 */
export const listProduct = (data) => qsget('/system/product/list', data);

/**
 * 通过 id 获取 product
 * @param { Number } productId
 * @returns { Promise<unknown> }
 */
export const queryProduct = (productId) => get(`/system/product/${productId}`);

/**
 * 获取 type 信息
 * @returns { Promise<unknown> }
 */
export const getTypes = () => get('/system/product/');

/**
 * 通过 data 对象保存 product, id 为空
 * @param { Object } data = product 对象
 * @returns {Promise<unknown>}
 */
export const addProduct = (data) => post('/system/product', data);

/**
 * 通过 data 对象修改 product, id 不为空
 * @param { Object } data = product 对象
 * @returns {Promise<unknown>}
 */
export const editProduct = (data) => put('/system/product', data);

/**
 * 通过 data 列表删除 product
 * @param { Object } data = { productIds: [...]}
 * @returns {Promise<unknown>}
 */
export const removeProduct = (data) => qsdeletefn(`/system/product`, data);
