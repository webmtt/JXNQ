export const coverRateOption = {
    tooltip: {
        trigger: 'axis',
        // formatter: '{a} <br/>{c} %'
    },
    color: ['#ff8763','#a2f774', '#62f3dd'],
    legend: {
        right: 0,
        top: 5,
        icon: 'circle',
        itemWidth: 7,
        textStyle: {color: '#fff'}
    },
    textStyle: {
        color: '#fff'
    },
    grid: {
        left: '0%',
        right: '8%',
        bottom: '5%',
        top: '20%',
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