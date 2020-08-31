import Tool from './Tool';

export default class MeasureAreaTool extends Tool {
    constructor() {
        super();
        this.name = 'MeasureAreaTool';
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

    // 添加已绘制的面图层;
    addDrawedLinesLayer() {
        this.map.addLayer({
            id: 'drawed-lines',
            type: 'fill',
            source: 'geojson',
            'paint': {
                'fill-color': '#ffe862',
                'fill-outline-color': 'transparent',
                'fill-opacity': 0.6
              },
            filter: ['in', '$type', 'Polygon'],
        });
    }

    // 添加已绘制的线图层;
    addDrawingLinesLayer() {
        this.map.addLayer({
            id: 'drawing-lines',
            type: 'line',
            source: 'geojson',
            layout: {'line-cap': 'round','line-join': 'round'},
            paint: {'line-color': '#fff','line-width': 1.5, 'line-dasharray': [2, 5]},
            filter: [
                'all',
                ['in', '$type', 'LineString']
            ]
        });
    }

    addPointsLayer() {
        this.map.addLayer({
            id: 'measure-points',
            type: 'symbol',
            source: 'geojson',
            layout: {
                "text-field": "{area}",
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
            filter: ['all',
                ['in', '$type', 'Point'],
            ]
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
    getArea() {
        const points = this.geoJson.features.filter(item => item.geometry.type === 'Point' && !item.properties.isCenter);
        if (points.length > 3) {
            const polygon = this.turf.polygon([points.map(pointItem => pointItem.geometry.coordinates)]);
            return (this.turf.area(polygon) / Math.pow(10,6)).toFixed(2)+'平方公里';
        }
        return 0;
    }

    onClick(event) {
        if (!this.running) return;
        
        this.geoJson.features = this.geoJson.features.filter(item => item.geometry.type === 'Point' && (!item.properties.isCenter && !item.properties.movePoint));

        const point = {
            'type': 'Feature',
            'geometry': {
                'type': 'Point',
                'coordinates': [event.lngLat.lng, event.lngLat.lat]
            },
            'properties': {
                'id': String(new Date().getTime()),
                'movePoint': false
            }
        };

        if (this.geoJson.features.length >= 3) {
            this.geoJson.features.splice(this.geoJson.features.length - 1, 0, point);
        } else {
            this.geoJson.features.push(point);
        }

        // 少于两个点
        if (this.geoJson.features.length < 2) return;

        if (this.geoJson.features.length > 3) {
            const polygon = this.turf.polygon([this.geoJson.features.map(pointItem => pointItem.geometry.coordinates)]);
            this.geoJson.features.push(polygon);

            // 计算多边形的中心点位和多边形的面积
            const centroid = this.turf.centroid(polygon);
            centroid.properties.isCenter = true;
            centroid.properties.area = this.getArea();
            this.geoJson.features.push(centroid);
            
        }

        let line = null;
        const coordinates = this.geoJson.features.map(pointItem => pointItem.geometry.coordinates);
        if (coordinates.length > 3) {
            line = this.turf.lineString(coordinates.slice(0, coordinates.length - 1))
        } else {
            line = this.turf.lineString(coordinates);
        }
        this.geoJson.features.push(line);

        this.map.getSource('geojson').setData(this.geoJson);
    }

    onMove(event) {
        if (!this.running) return;
        if (!this.geoJson.features.length) return;

        this.geoJson.features = this.geoJson.features.filter(item => item.geometry.type !== 'Polygon' && item.geometry.type !== 'LineString' && !item.properties.isCenter);

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

        let movePointIndex = -1;
        this.geoJson.features.forEach((item, index) => {
            if (item.geometry.type === 'Point' && item.properties.movePoint) {
                movePointIndex = index;
            }
        })

        if (movePointIndex > -1) {
            this.geoJson.features.splice(movePointIndex, 1, point);
        } else {
            if (this.geoJson.features<4) {
                this.geoJson.features.push(point);
            } else {
                this.geoJson.features.splice(this.geoJson.features.length-1,0,point);
            }
        }

        // 如果有三个点就可形成面;
        if (this.geoJson.features.length === 3) {
            this.geoJson.features.push(this.geoJson.features[0])
        }

        if (this.geoJson.features.length > 3) {
            const polygon = this.turf.polygon([this.geoJson.features.map(pointItem => pointItem.geometry.coordinates)]);
            this.geoJson.features.push(polygon);

            // 计算多边形的中心点位和多边形的面积
            const centroid = this.turf.centroid(polygon);
            centroid.properties.isCenter = true;
            centroid.properties.area = this.getArea();
            this.geoJson.features.push(centroid);
            const length = this.geoJson.features.length - 1
            const line = this.turf.lineString(this.geoJson.features.slice(0,length).map(pointItem => pointItem.geometry.coordinates));
            this.geoJson.features.push(line);
        } else {
            const line = this.turf.lineString(this.geoJson.features.map(pointItem => pointItem.geometry.coordinates));
            this.geoJson.features.push(line);
        }

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