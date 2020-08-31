import request2 from '@/utils/request2'
// import request from '@/utils/request'

export function loginIn(params) {
  return request2({
    url: '/user/login',
    method: 'get',
    params
  })
}

export function getInfo(params) {
  return request2({
    url: '/user/findbyid',
    method: 'get',
    params
  })
}

// 注册用户
export function register(params) {
  return request2({
    url: '/user/register',
    method: 'post',
    params
  })
}

export function logout() {
  return request2({
    url: '/user/logout',
    method: 'post'
  })
}

export function userList(params) {
  return request2({
    url: '/user/findall',
    method: 'get',
    params
  })
}

//新增用户
export function addUser(params) {
  return request2({
    url: 'user/adduser',
    method: 'post',
    params
  })
}
//删除用户可批量删除
export function deleteUser(params){
  return request2({
    url: 'user/deletebyid',
    method: 'post',
    params
  })

}
//编辑用户
export function editUser(params){
  return request2({
    url: 'user/updateuser',
    method: 'post',
    params
  })
}

//用户的基本信息
export function userDetail(params){
  return request2({
    url: 'user/findbyid',
    method: 'get',
    params
  })
}

export function modifyPassword(params){
  return request2({
    url: 'user/changepwd',
    method: 'post',
    params
  })
}
