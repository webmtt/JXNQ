export const weekWeatherOption = {
    tooltip: {
        trigger: 'axis'
    },
    color: ['#a2f774', '#ff8763', '#62f3dd'],
    textStyle: {
        color: '#fff'
    },
    grid: {
        left: '-4%',
        right: '6%',
        bottom: '0',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        show: false,
        boundaryGap: false,
    },
    yAxis: {
        type: 'value',
        show: false,
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
        }
    ]
}