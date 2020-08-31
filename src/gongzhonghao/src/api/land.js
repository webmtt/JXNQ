import request from '@/utils/request'

export function getLandList(params) {
  return request({
    url: '/officialAccounts/queryLand',
    method: 'get',
    params
  })
}

export function addLand(data) {
  return request({
    url: '/officialAccounts/landBind',
    method: 'post',
    data
  })
}

export function editLand(data) {
  return request({
    url: '/officialAccounts/landEdit',
    method: 'post',
    data
  })
}

export function deleteLand(params) {
  return request({
    url: '/officialAccounts/landDel',
    method: 'get',
    params
  })
}