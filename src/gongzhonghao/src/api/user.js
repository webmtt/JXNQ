import request from '@/utils/request'


// 注册用户
export function register(data) {
  return request({
    url: '/officialAccounts/userRegister',
    method: 'post',
    data
  })
}

export function loginIn(data) {
  return request({
    url: '/officialAccounts/userLogin', // /user/login
    method: 'post',
    data
  })
}

export function logout() {
  return Promise.resolve();
}

export function getInfo(params) {
  return request({
    url: '/officialAccounts/userInfo',
    method: 'get',
    params
  })
}
