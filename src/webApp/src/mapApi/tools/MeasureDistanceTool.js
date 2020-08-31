import Tool from './Tool';
import { Point } from 'mapbox-gl';

export default class MeasureDistanceTool extends Tool {
    constructor() {
        super();
        this.name = 'MeasureDistanceTool';
        this.geoJson = { 'type': 'FeatureCollection', 'features': [] };
    }
    
    // 绑定启动dom
    _bindEvent() {
        this.initTool();
        
        this.map.on('click', this.onClick);
        this.map.on('mousemove', this.onMove);
        this.map.on('dblclick', this.onDbClick);
    }

    _unbindEvents() {
        this.destroyTool();
        this.map.off('click', this.onClick);
        this.map.off('mousemove', this.onMove);
        this.map.off('dblclick', this.onDbClick);
    }

    // 添加已绘制的的线图层;
    addDrawedLinesLayer() {
        this.map.addLayer({
            id: 'drawed-lines',
            type: 'line',
            source: 'geojson',
            layout: {'line-cap': 'round','line-join': 'round'},
            paint: {'line-color': 'green','line-width': 1.5},
            filter: [
                'all',
                ['in', '$type', 'LineString'],
                ['!=', 'movingLine', true]
            ]
        });
    }

    addDrawingLinesLayer() {
        this.map.addLayer({
            id: 'drawing-lines',
            type: 'line',
            source: 'geojson',
            layout: {'line-cap': 'round','line-join': 'round'},
            paint: {'line-color': 'red','line-width': 1.5, 'line-dasharray': [5, 5]},
            filter: [
                'all',
                ['in', '$type', 'LineString'],
                ['==', 'movingLine', true]
            ]
        });
    }

    addPointsLayer() {
        this.map.addLayer({
            id: 'measure-points',
            type: 'symbol',
            source: 'geojson',
            layout: {
                "text-field": "{length}",
                "text-anchor": "top",
                "text-size": 12,
                "text-offset": [0, 1],
                "icon-image": "circle-11",
            },
            paint: {
                "text-color": "#fff",
                "text-halo-width": 2,
                "text-halo-color": "rgb(11, 148, 68)"
            },
            filter: ['in', '$type', 'Point']
          });
    }

    initTool() {
        this.map.addSource('geojson', {'type': 'geojson','data': this.geoJson});
        
        this.addDrawedLinesLayer();
        this.addDrawingLinesLayer();
        this.addPointsLayer();
    }

    
    /**
     * 计算点的长度;
     * @method getLength
     */
    getLength() {
        const points = this.geoJson.features.filter(item => item.geometry.type === 'Point');
        if (points.length > 1) {
            const lines = this.turf.lineString(points.map(pointItem => pointItem.geometry.coordinates));
            return this.turf.length(lines, {units: 'kilometers'});
        }
        return 0;
    }

    onClick(event) {
        if (!this.running) return;
        
        this.geoJson.features = this.geoJson.features.filter(item => item.geometry.type === 'Point' && !item.properties.movePoint);
        this.geoJson.features = this.geoJson.features.filter(item => item.geometry.type !== 'LineString');

        const point = {
            'type': 'Feature',
            'geometry': {
                'type': 'Point',
                'coordinates': [event.lngLat.lng, event.lngLat.lat]
            },
            'properties': {
                'id': String(new Date().getTime()),
                'length': 0,
                'movePoint': false
            }
        };

        const prePoint = this.geoJson.features[this.geoJson.features.length - 1] || null;
        if (!prePoint || (prePoint.geometry.coordinates[0] != point.geometry.coordinates[0] && prePoint.geometry.coordinates[1] != point.geometry.coordinates[1])) {
            this.geoJson.features.push(point);
        }

        point.properties.length = this.getLength().toFixed(2) + '公里';

        const points = [...this.geoJson.features];
        if (points.length > 1) {
            const lines = this.turf.lineString(points.map(pointItem => pointItem.geometry.coordinates));
            this.geoJson.features.push(lines);
        }
        

        this.map.getSource('geojson').setData(this.geoJson);
    }

    onMove(event) {
        if (!this.running) return;
        if (!this.geoJson.features.length) return;

        const point = {
            'type': 'Feature',
            'geometry': {
                'type': 'Point',
                'coordinates': [event.lngLat.lng, event.lngLat.lat]
            },
            'properties': {
                'id': String(new Date().getTime()),
                'length': 0,
                'movePoint': true
            }
        };
        this.geoJson.features = this.geoJson.features.filter(item => !(item.geometry.type === 'LineString' && item.properties.movingLine));

        const hasMovePoint = this.geoJson.features.filter(item => item.geometry.type === 'Point' && item.properties.movePoint).length;
        if (hasMovePoint) {
            this.geoJson.features.splice(this.geoJson.features.length - 1, 1, point);
        } else {
            this.geoJson.features.splice(this.geoJson.features.length, 0, point);
        }
        point.properties.length = this.getLength().toFixed(2) + '公里';
        
        // 添加正在移动的线;
        const moveLinePoints = this.geoJson.features.filter(item => item.geometry.type === 'Point').slice(-2);
        const lines = this.turf.lineString(moveLinePoints.map(item => item.geometry.coordinates));
        lines.properties.movingLine = true;
        this.geoJson.features.push(lines);

        this.map.getSource('geojson').setData(this.geoJson);
    }

    destroyTool() {
        this.map.getLayer('measure-points') && this.map.removeLayer('measure-points');
        this.map.getLayer('drawing-lines') && this.map.removeLayer('drawing-lines');
        this.map.getLayer('drawed-lines') && this.map.removeLayer('drawed-lines');
        this.map.getSource('geojson') && this.map.removeSource('geojson');
        this.geoJson.features.length = [];
    }

    onDbClick(event) {
        if (!this.running) return;
        this.running = false;
    }

}