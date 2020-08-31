<template>
    <div v-if="times.length" class="playBox">
        <div v-if="years.length" class="activeYear">
            <el-dropdown @change="yearChanged">
                <span style="color: #fff">
                    {{activeYear}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="(item, index) in years" :key="index">{{item.value}}</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div @click="togglePlay" class="toggleButton">
            <i :class="{'el-icon-video-pause': status, 'el-icon-video-play': !status}"></i>
        </div>
        <div class="timeLine">
            <el-tooltip v-for="(item, k) in times" :key="k" effect="dark" :content="item" placement="top">
                <div @click="timeLineTabChange(k)" :class="{'timeBar': true, 'defaultBg': k != currentIndex,'activeBg': k == currentIndex}">
                    <span style="position: absolute; bottom: 10px;color: #17cdbd">{{getPeriod(item.split('_')[0],item.split('_')[2], item.split('_')[3])}}</span>
                </div>
            </el-tooltip>
        </div>
    </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
export default {
    name: 'timePlayer',
    data() {
        return {
            status: false,
            currentIndex: 0,
            timer: null,
            activeYear: '2019',
            times: [],
            isMonthPlay: false,
            years: []
        };
    },
    watch: {
        playLayers: {
            handler(value){
              this.times = value;
              this.stop();
              this.currentIndex = 0;
            },
            immediate: true
        },
        playerYears: {
            handler(value){
                console.log(value)
              this.years = value;
            },
            immediate: true
        },
        '$route.path'() {
            this.stop();
            this.currentIndex = 0;
        },
        currentIndex(index) {
            this.$store.dispatch('map/changeActiveLayers', {key: 'showLayers', value: [this.times[index]]});
        }
    },
    computed: {
        ...mapState('map', ['lengendLayers', 'playLayers','playerYears','businessLayers'])
    },
    methods: {
        getPeriod(type, monthStr, dayStr) {
            if (this.$route.path !== '/growManage') return '';
            const month = parseInt(monthStr);
            const day = parseInt(dayStr);
            if (type === '油菜') {
                if ((month === 9 && day >= 20) || (month == 10 && day <= 1)) {
                    return '播种期'
                }
                if ((month === 10 && day >= 2) || (month == 12 && day <= 28) || month === 11) {
                    return '苗期'
                }
                if ((month === 12 && day >= 29) || (month == 1 && day <= 26)) {
                    return '蕾苔期'
                }
                if ((month === 1 && day >= 27) || (month == 3 && day <= 1) || month === 2) {
                    return '苗期'
                }
                if ((month === 3 && day >= 2 && day <= 31)) {
                    return '蕾苔期'
                }
                if ((month === 4 && day >= 1 && day <= 28)) {
                    return '蕾苔期'
                }
            } else if (type === '小麦') {
                if (month === 10 && day >= 12 && day <= 22) {
                    return '播种期'
                }
                if ((month === 10 && day >= 23) || (month == 11 && day <= 15)) {
                    return '出苗期'
                }
                if (month === 11 && day >= 16 && day <= 31) {
                    return '分蘖期'
                }
                if ((month === 12 && day >= 1) || (month == 1 && day <= 25)) {
                    return '越冬期'
                }
                if ((month === 1 && day >= 26) || (month == 2 && day <= 26)) {
                    return '返青期'
                }
                if ((month === 2 && day >= 27) || (month == 3 && day <= 25)) {
                    return '拔节期'
                }
                if ((month === 3 && day >= 26) || (month == 4 && day <= 1)) {
                    return '孕穗期'
                }
                if (month === 4 && day >= 2 && day <= 12) {
                    return '抽穗期'
                }
                if (month === 4 && day >= 13 && day <= 30) {
                    return '开花期'
                }
                if ((month === 4 && day >= 31) || (month == 5 && day <= 6)) {
                    return '灌浆期'
                }
                if (month === 5 && day >= 7 && day <= 15) {
                    return '成熟期'
                }
                if (month === 16 && day >= 7 && day <= 24) {
                    return '收割期'
                }
            } else {
                if ((month === 5 && day >= 25) || (month == 6 && day <= 5)) {
                    return '播种期'
                }
                if ((month === 6 && day >= 6) || (month == 7 && day <= 26)) {
                    return '分蘖/移栽期'
                }
                if ((month === 7 && day >= 27) || (month == 8 && day <= 31)) {
                    return '拔节期'
                }
                if (month === 9 && day >= 1 && day <= 19) {
                    return '抽穗期'
                }
                if (month === 9 && day >= 20 && day <= 30) {
                    return '乳熟期'
                }
                if ((month === 9 && day >= 31) || (month == 10 && day <= 23)) {
                    return '收割期'
                }
            }
            return '';
        },
        /**
         * 停止播放,并销毁定时器;
         * @method stop
         */
        stop () {
            this.status = false;
            this.timer && window.clearInterval(this.timer);
        },

        /**
         * 播放或者暂停播放器;
         * @method togglePlay
         */
        togglePlay() {
            this.status = !this.status;
            if (!this.status) return this.stop();

            this.timer = setInterval(() => {
                this.currentIndex++;
                this.currentIndex = this.currentIndex % this.times.length;
            }, 2500);
        },
        timeLineTabChange (k) {
            this.stop();
            this.currentIndex = k;
        },
        yearChanged (value) {
            const showLayers = this.businessLayers.filter(item => item.split('_')[1] === value);
            this.$store.dispatch('map/changePlayYears', {key: 'playerYears', value: showLayers})
                .then(res => {
                    this.$store.dispatch('map/changeActiveLayers', {key: 'showLayers', value: [showLayers[0]]});
                })
        }
    }
};
</script>

<style lang="scss" scoped>
    .playBox {
        margin: 0 auto;
        left: 0;
        right: 0;
        padding: 5px 15px 0 15px;
        width: 70%;
        height: 60px;
        background: #282d26e3;
        position: absolute;
        z-index: 100;bottom: 10px;
        border-radius: 5px;
        display: flex;
        flex-direction: row;
        align-items: center;
    }
    .activeYear{
        width: 70px;
        color: #fff;
        padding: 3px;
        border-radius: 10px;
        text-align: center;
        border: 1px solid #fff;
    }
    .toggleButton {
        width: 40px;
        font-size: 24px;
        color: #fff;
        text-align: center;
        cursor: pointer
    }
    .timeLine {
        flex: 1;
        display: flex;
        flex-direction: row;
        border: 1px solid #ffffff;
        border-radius: 4px;
    }
    .timeBar {
        flex: 1;
        height: 6px;
        position: relative;
        text-align: center;
        border-right: 1px solid #cccccc
    }
    .timeBar:first-child {
        border-left-style: none
    }
    .timeBar:last-child {
        border-right-style: none
    }
    .defaultBg {
        background: transparent
    }
    .activeBg {
        background: #fff
    }
</style>
