<template>
  <div class="register">
    <div class="content-part">
      <h2>{{landModel.id ? '编辑地块' : '新增地块'}}</h2>

      <mt-field label="种植作物：" :disableClear="true" placeholder="请输入用户名" :readonly="true" @focus.native.capture="formChange('cropOptions')" v-model="landModel.crops"></mt-field>
      <mt-field label="种植面积：" :disableClear="true" placeholder="请输入种植面积" type="number" v-model.number="landModel.area"></mt-field>

      <mt-field label="选择城市：" :disableClear="true" placeholder="选择城市" :readonly="true" v-model="landModel.city"></mt-field>
      <mt-field label="选择区县：" :disableClear="true" placeholder="选择区县" :readonly="true" @focus.native.capture="formChange('countryOptions')" v-model="landModel.district"></mt-field>
      <mt-field label="详细地址：" :disableClear="true" placeholder="详细地址" v-model="landModel.address"></mt-field>

      <div class="fn-button">
        <mt-button type="primary" @click="submitData" size="large">确 定</mt-button>
      </div>
    </div>

    <mt-popup :closeOnClickModal="false" style="width: 100%" v-model="popupVisible" position="bottom" class="mint-popup">
      <mt-picker :slots="pickerOptins" @change="onValuesChange" :show-toolbar="true" ref="picker">
        <mt-button type="primary" style="position: absolute;right: 5px;top: 5px;" size="small" @click="handleConfirm">确认</mt-button>
      </mt-picker>
    </mt-popup>
  </div>
</template>

<script>
import { validZhUsername, validPhone } from '@/utils/validate';
import { addLand, editLand } from '@/api/land';
import { Toast, Indicator, MessageBox } from 'mint-ui';
export default {
  name: 'register',
  data () {
    return {
      popupVisible: false,
      landModel: {
        id: '',
        crops: '',
        city: '嘉兴市',
        district: '',
        address: '',
        area: ''
      },
      whichPicker: '',
      pickerOptins: null,
      cropOptions: [
        {
          values: ['水稻', '油菜', '小麦'],
          textAlign: 'center'
        }
      ],
      countryOptions: [{
          values: ['南湖区', '秀洲区', '嘉善县', '海盐县', '海宁市', '平湖市', '桐乡市'],
          textAlign: 'center'
      }]
    }
  },
  methods: {
    /**
     * 新增或编辑地块
     * @method deleteLand
     */
    formChange(options) {
      this.popupVisible = true;
      this.whichPicker = options;
      this.pickerOptins = this[options];
    },

    beforeSubmit() {
      let errMessage = ''
      if (!this.landModel.area || this.landModel.area <= 0) {
        errMessage = '面积输入不合法'
      }
      return errMessage;
    },

    submitData() {
      const errMessage = this.beforeSubmit();
      if (errMessage) {
        return Toast({ message: errMessage, duration: 2 * 1000 })
      }
      const methodName = this.landModel.id ? editLand : addLand;
      Indicator.open();
      methodName(this.landModel)
        .then(res => MessageBox.alert(`${this.landModel.id ? '编辑': '新增'}地块成功`, '提示'))
        .then(res => this.$router.push({ path: '/' }))
        .catch(err => {})
        .finally(res => Indicator.close());
    },

    handleConfirm(){
      this.popupVisible = false;
    },

    onValuesChange(picker, values) {
      if (this.whichPicker === 'cropOptions') {
        this.landModel.crops = values[0]
      } else {
        this.landModel.district = values[0]
      }
    }
  },
  mounted() {
    this.landModel = Object.assign({}, this.landModel, this.$route.query.data)
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.register {
  padding: 0 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  .content-part {
    width: 90%;
    margin-top: -120px;
  }
}
.fn-button {
  padding: 20px 10px;
}
a {
  color: #42b983;
}
</style>
