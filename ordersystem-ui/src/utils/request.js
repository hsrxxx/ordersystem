/**
 * axios封装
 * 请求拦截、相应拦截、错误统一处理
 */
import axios from 'axios';
import QS from 'qs';
import { Message, Loading, Notification, MessageBox } from 'element-ui';
import { getToken, removeToken } from '@/utils/auth'
import store from '../store'
import router from "vue-router";
import { tansParams } from "@/utils/common";
 
// let loadinginstace

// 环境的切换
if (process.env.NODE_ENV === 'development') {
    axios.defaults.baseURL = '';
} else if (process.env.NODE_ENV === 'debug') {
    axios.defaults.baseURL = '';
} else if (process.env.NODE_ENV === 'production') {
    axios.defaults.baseURL = '';
}
 
// 请求超时时间
axios.defaults.timeout = 10000;
 
// post请求头
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
 
// 请求拦截器
axios.interceptors.request.use(    
    config => {
        (getToken()) && (config.headers.Authorization = 'Bearer ' + getToken())
        return config;    
    },    
    error => {
        return Promise.error(error);    
    })
 
// 响应拦截器
axios.interceptors.response.use(
    response => {
        const code = response.data.code || 200;
        if (code === 401){
            Message.error(response.data.msg)
            removeToken()
            location.href = "/"
        } else if (code === 500){
            Message.error(response.data.msg)
            if (response.data.msg === '令牌不能为空'){
                removeToken()
                location.href = "/"
            } else if (response.data.msg === '登录状态已过期'){
                removeToken()
                location.href = "/"
            } else if (response.data.msg === '令牌验证失败'){
                removeToken()
                location.href = "/"
            }
            return Promise.reject(response.data.msg);
        } else if (code !== 200){
            Notification.error({
                title: response.data.msg
            })
            return Promise.resolve(response.data.msg);
        } else {
            return Promise.resolve(response);
        }
    },
    error => {
        // loadinginstace && loadinginstace.close()
        return Promise.reject(error.response);
    },
);

// 通用下载方法
export function download(url, params, filename) {
    return axios.post(url, params, {
        transformRequest: [(params) => {
            return tansParams(params)
        }],
        responseType: 'blob'
    }).then((data) => {
        const blob = new Blob([data.data], {type: "application/vnd.ms-excel"})
        if ('download' in document.createElement('a')) {
            const elink = document.createElement('a')
            elink.download = filename
            elink.style.display = 'none'
            elink.href = URL.createObjectURL(blob)
            document.body.appendChild(elink)
            elink.click()
            URL.revokeObjectURL(elink.href)
            document.body.removeChild(elink)
        } else {
            navigator.msSaveBlob(blob, filename)
        }
    }).catch((r) => {
        console.error(r)
    })
}

/** 
 * get方法，对应get请求 
 * @param { String } url [请求的url地址] 
 * @param { Object } params [请求时携带的参数] 
 */
export function get(url, params){
    return new Promise((resolve, reject) =>{
        axios.get(url, { params: params })
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                console.log(err)
                reject(err.data)
            })
    })
}

/**
 * get方法，对应get请求
 * @param { String } url [请求的url地址]
 * @param { Object } params [请求时携带的参数]
 */
export function qsget(url, params){
    return new Promise((resolve, reject) =>{
        url = url + '?' + QS.stringify(params)
        axios.get(url)
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}

/** 
 * post方法，对应post请求 
 * @param { String } url [ 请求的url地址 ] 
 * @param { Object } params [ 请求时携带的参数 ] 
 */
export function post(url, params) {
    return new Promise((resolve, reject) => {
        axios.post(url, params)
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}


/** 
 * post方法，对应post请求 
 * @param { String } url [ 请求的url地址 ] 
 * @param { Object } params [ 请求时携带的参数 ] 
 */
export function qspost(url, params) {
    return new Promise((resolve, reject) => {
        axios.post(url, QS.stringify(params))
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}

/**
 * put方法，对应put请求
 * @param { String } url [ 请求的url地址 ]
 * @param { Object } params [ 请求时携带的参数 ]
 */
export function put(url, params) {
    return new Promise((resolve, reject) => {
        axios.put(url, params)
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err.data)
            })
    });
}
    

/**
 * qsput方法，对应put请求，参数进行qs处理
 * @param { String } url [ 请求的url地址 ]
 * @param { Object } params [ 请求时携带的参数 ]
 */
export function qsput(url, params) {
    return new Promise((resolve, reject) => {
        axios.put(url, QS.stringify(params))
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err.data)
            })
    });
}

/**
 * delete方法，对应delete请求
 * @param { String } url [ 请求的url地址 ]
 * @param { Object } params [ 请求时携带的参数 ]
 */
export function deletefn(url, params) {
    return new Promise((resolve, reject) => {
        axios.delete(url, params)
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}

/** 
 * qsdeletefn方法，对应delete请求，参数进行qs处理
 * @param { String } url [ 请求的url地址 ] 
 * @param { Object } params [ 请求时携带的参数 ] 
 */
export function qsdeletefn(url, params) {
    return new Promise((resolve, reject) => {
        axios.delete(
            url,
            {
                params,
                paramsSerializer: params => {
                    return QS.stringify(params, { indices: false })
                }
            })
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}
