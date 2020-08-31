import request2 from '@/utils/request2'

// 按已选择地区名，作物名，以及年份查询该作物种植面积接口
export function getSeasonsData(params) {
  return request2({
    url: '/plantCover/coverRate',
    method: 'get',
    params
  })
}

export function getLayerDetail(params) {
  return request2({
    url: '/plantCover/imageQuery',
    method: 'get',
    params
  })
}

export function updateLayerDetail(data, headers) {
  return request2({
    url: '/plantCover/infoInput',
    method: 'post',
    data,
    headers
  })
}
