import Mock from 'mockjs'

const details = ["建设街道", "解放街道", "新嘉街道", "南湖街道", "新兴街道", "城南街道", "东栅街道", "长水街道", "七星街道", "凤桥镇", "余新镇", "新丰镇", "大桥镇", "新城街道", "嘉北街道", "塘汇街道", "高照街道", "王江泾镇", "油车港镇", "新塍镇", "王店镇", "洪合镇", "魏塘街道", "罗星街道", "惠民街道", "大云镇", "西塘镇", "干窑镇", "陶庄镇", "姚庄镇", "天凝镇", "武原街道", "西塘桥街道", "秦山街道", "望海街道", "沈荡镇", "百步镇", "于城镇", "澉浦镇", "通元镇", "硖石街道", "海洲街道", "海昌街道", "马桥街道", "许村镇", "长安镇", "周王庙镇", "丁桥镇", "斜桥镇", "黄湾镇", "盐官镇", "袁花镇", "当湖街道", "钟埭街道", "曹桥街道", "乍浦镇", "新埭镇", "新仓镇", "广陈镇", "林埭镇", "独山港镇", "梧桐街道", "凤鸣街道", "高桥街道", "乌镇镇", "濮院镇", "屠甸镇", "石门镇", "河山镇", "洲泉镇", "大麻镇", "崇福镇"];

const data = Mock.mock({
  'items|20': [{
    id: '@id',
    'crop|1': ['水稻', '玉米', '小麦'],
    city: '嘉兴市',
    'country|1': ['南湖区','秀洲区','嘉善县','海盐县','海宁市','平湖市','桐乡市'],
    'detail|1': details,
    area: '@integer(300, 5000)'
  }]
})

export default [
  {
    url: '/land/list',
    type: 'get',
    response: config => {
      const size = parseInt(config.query.pageSize);
      const page = parseInt(config.query.page);

      const start = (page - 1) * size;
      const end = start + size;
      const items = data.items.slice(start, end);
      
      return {
        code: 20000,
        data: {
          total: 20,
          items: items
        }
      }
    }
  }
]
