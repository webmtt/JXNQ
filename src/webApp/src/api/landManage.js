import request2 from '@/utils/request2'

// 按已选择地区名，作物名，以及年份查询该作物种植面积接口
export function getAreaStatics(params) {
  return request2({
    url: '/landManage/query',
    method: 'get',
    params
  })
}

export function getCropByDistrict(params) {
  return request2({
    url: '/landManage/getCropByDistrict',
    method: 'get',
    params
  })
}

export function getcropLineChart(params) {
  return request2({
    url: '/landManage/cropLineChart',
    method: 'get',
    params
  })
}
