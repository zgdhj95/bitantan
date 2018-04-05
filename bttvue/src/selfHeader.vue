<template>
  <div>
    <div class="self-header__container">
      <input type="text" placeholder="请输入要查询的币种名称" v-model="userid" class="search-input" @keyup.enter="queryAsset($event)"/>
      <!-- <span class="self-title">自选行情</span>
      <div class="self-rmb">
        <img v-show="showRmb" class="switchicon" src="https://static.weixiaotong.com.cn/explorer-static/switchon.png" @click="toggleShowRmb"/>
        <img v-show="!showRmb" class="switchicon" src="https://static.weixiaotong.com.cn/explorer-static/switchoff.png" @click="toggleShowRmb"/>
        <span v-show="showRmb" class="rmb-on-text" @click="toggleShowRmb">人民币</span>
        <span v-show="!showRmb" class="rmb-off-text" @click="toggleShowRmb">人民币</span>
        <span class=""></span>
      </div> -->
    </div>
    <div class="self-header__container" v-if="showAsset">
      <span class="asset-font">我的资产（btc）: {{asset}}</span>
    </div>
  </div>

</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      showRmb: true,
      usid: '',
      asset: 0,
      showAsset: false
    }
  },
  methods: {
    queryAsset (ev) {
      // 调用应用实例的方法获取全局数据
      axios.get('http://123.207.241.107/asset?userid=' + this.userid)
        .then(response => {
          if (response.data === '0' || response.data === null || response.data === undefined) {
            this.showAsset = false
            return
          }
          console.log('resp = ', response.data)
          console.log('this is', this)
          this.asset = parseFloat(response.data)
          this.showAsset = true
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    toggleShowRmb () {
      this.showRmb = !this.showRmb
    }
  },
  created () {
    // 调用应用实例的方法获取全局数据
  }
}
</script>

<style scoped>

  .search-input {
      width: calc( 100% - 0px);
      border: 1rpx solid #BABABA;
      border-radius: 4px;
      padding: 3px 5px;
      font-size: 14px;
      text-align: center;
  }

  .self-header__container {
    display: flex;
    padding: 10px;
    border-bottom: 1rpx solid #DADADA;
    justify-content: space-between;
  }

  .self-rmb img {
    width: 22px;
    height: 32px;
  }

  .self-title {
    font-size: 24px;
    font-weight: 600;
    color: #1D3F59;
  }

  .self-rmb {
    display: flex;
    justify-content: center;
    width: 30px;
  }

  .rmb-on-text {
    font-size: 16px;
    color: #1D3F59;
    margin-left: 5px;
  }

  .rmb-off-text {
    font-size: 16px;
    color: #C6CFD6;
    margin-left: 5px;
  }

  .switchicon {
    width: 36px;
    height: 18px;
  }

  .asset-font {
    font-size: 16px;
    color: #ffffff;
    margin-left: 5px;
  }
</style>
