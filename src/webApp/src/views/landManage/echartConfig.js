
  export const percentageOption = {
    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    color: ['#ff88d4', '#8fafff', '#85d4fc', '#50de6a', '#ffe862', '#ffad88', '#ff8181'].reverse(),
    series: [
        {
            name: '作物面积占比：',
            type: 'pie',
            radius: ['60%', '85%'],
            avoidLabelOverlap: true,
            label: {
                normal: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    show: true,
                    formatter: ['{city|{b}}','{number|{c}}','{unit|(万亩)}'].join('\n'),
                    rich: {
                        city: {
                            fontSize: 12,
                        },
                        number: {
                            fontSize: 22,
                            fontWeight: 'bold',
                            height: 40
                        },
                        unit: {
                            fontSize: 12,
                        }
                    },
                    textStyle: { color: '#fff' }
                }
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
            itemStyle: {borderWidth: 2, borderColor: '#383d41'},
            data: [
                {value: 999, name: '南湖区'},
                {value: 310, name: '秀洲区'},
                {value: 234, name: '嘉善县'},
                {value: 135, name: '海盐县'},
                {value: 335, name: '海宁市'},
                {value: 135, name: '平湖市'},
                {value: 335, name: '桐乡市'}
            ]
        }
    ]
  };

  export const areaStaticsOption = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    color: ['#ff8763','#a2f774', '#62f3dd'],
    legend: {
        right: 0,
        icon: 'circle',
        itemWidth: 7,
        data: ['水稻', '油菜', '小麦'],
        textStyle: {color: '#fff'}
    },
    textStyle: {
        color: '#fff'
    },
    grid: {
        left: '0%',
        right: 0,
        bottom: '5%',
        top: '10%',
        containLabel: true,
    },
    xAxis: {    
        type: 'category',
        axisLine:{
            lineStyle:{ color:'#fff' }
        },
        splitLine: {
            show: false
        },
        axisLabel:{interval: 0}
    },
    yAxis: {
        type: 'value',
        axisLine:{
            lineStyle:{ color:'#fff' }
        },
        splitLine: {
            show: false
        },
    },
    dataset: {
        source:{}
    },
    series: [
        {
            type: 'bar',
            stack: '总量',
            barWidth: 14,
            itemStyle: {
                borderColor: '#383d41',
                borderWidth: 2
            }
        },
        {
            type: 'bar',
            stack: '总量',
            itemStyle: {
                borderColor: '#383d41',
                borderWidth: 2
            }
        },
        {
            type: 'bar',
            stack: '总量',
            itemStyle: {
                borderColor: '#383d41',
                borderWidth: 2
            }
        }
    ]
};
  
  export const areaChangeOption = {
    tooltip: {
        trigger: 'axis'
    },
    color: ['#ff8763','#a2f774', '#62f3dd'],
    legend: {
        right: 0,
        top: 5,
        icon: 'circle',
        itemWidth: 7,
        data: ['水稻', '油菜', '小麦'],
        textStyle: {color: '#fff'}
    },
    textStyle: {
        color: '#fff'
    },
    grid: {
        left: '0%',
        right: '5%',
        bottom: '0%',
        top: '15%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        axisLine:{
            lineStyle:{ color:'#fff' }
        },
        splitLine: {
            show: true,
            lineStyle: { type: 'solid', color: '#666' }
        }
    },
    yAxis: {
        type: 'value',
        axisLine:{
            lineStyle:{ color:'#fff' }
        },
        splitLine: { show: false }
    },
    dataset: {
        source:{}
    },
    series: [
        {
            type: 'line',
            lineStyle: { width: 1 }
        },
        {
            type: 'line',
            lineStyle: { width: 1 }
        },
        {
            type: 'line',
            lineStyle: { width: 1 }
        }
    ]
}