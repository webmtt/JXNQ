import request2 from '@/utils/request2'

export function getStationInfo(params) {
  return request2({
    url: '/weather/queryDetail',
    method: 'get',
    params
  })
}


export function getWeatherImage(params) {
  return request2({
    url: '/weather/queryImage',
    method: 'get',
    params
  })
}

// 七天预报
export function queryWeather(params) {
  return request2({
    url: '/weather/querySevenDaysWeather',
    method: 'get',
    params
  })
}

export function getSatus(params) {
  return request2({
    url: '/weather/getStatusByWeathers',
    method: 'get',
    params
  })
}

export function getInfo(params) {
  return request2({
    url: '/weather/getParamsByWeather',
    method: 'get',
    params
  })
}

export function setWeatherConfig(params) {
  return request2({
    url: '/weather/getStatusByWeatherParams',
    method: 'get',
    params
  })
}