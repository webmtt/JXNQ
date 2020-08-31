// const baseUrl = 'http://10.136.5.68:20002/api/geo-web/layerData/getMapStyle?id=';// 内网地址
// const baseUrl = 'http://cloud.piesat.cn:9000/dataservices/map-editor/api/geo-web/layerData/getMapStyle?id=';
const baseUrl = 'http://121.36.21.73:20002/api/pie-cloud/layerData/layerTemplates?key=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpbmZvIjoie1widXNlcklkXCI6XCI1MjhcIixcImtleVwiOlwiOWE1YjliMzdhZDJmZGE2NWYxN2NlNGY1MzEyODdmYTZcIn0ifQ.gK7D8O6HqQnZeZaY5Uy4qLutbJRHBDWnKuhN53PeyuU&id=';

export const scenceConfigs = {
    landManage: {
        scenceUrl: baseUrl + '2c92808c71bafa7a01720c0fc70f00e6',
        scenceName: 'landManage',
        checkType: 'checkBox'
    },
    growManage: {
        scenceUrl: baseUrl + '2c92808c71bafa7a01720c456bda00f4',
        scenceName: 'growManage',
        checkType: 'checkBox'
    },
    vegetationManage: {
        scenceUrl: baseUrl + '2c92808c71bafa7a01720c461f7200f6',
        scenceName: 'vegetationManage',
        checkType: 'checkBox'
    },
    meteorologicalService: {
        scenceUrl: baseUrl + '2c92808c71bafa7a01720c46dc7300f8',
        scenceName: 'meteorologicalService',
        checkType: 'checkBox'
    }
}

export const baseMaps = {
    image: baseUrl + '2c92808c71bafa7a01720c4bb44500fb',
    street: baseUrl + '2c92808c71bafa7a01720c4d303900fd'
};