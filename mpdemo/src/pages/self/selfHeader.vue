<template>
  <div class="self-header__container">
    <input type="text" placeholder="请输入要添加的币种名称" :style="textAlign" class="search-input" @focus="focusSearch"/>
    <div class="delete-coin">
      <img src="https://static.weixiaotong.com.cn/ico_delete.svg" />
    </div>
    <!-- <span class="self-title">自选行情</span>
    <div class="delete-coin">
      <img v-show="showRmb" class="switchicon" src="https://static.weixiaotong.com.cn/explorer-static/switchon.png" @click="toggleShowRmb"/>
      <img v-show="!showRmb" class="switchicon" src="https://static.weixiaotong.com.cn/explorer-static/switchoff.png" @click="toggleShowRmb"/>
      <span v-show="showRmb" class="rmb-on-text" @click="toggleShowRmb">人民币</span>
      <span v-show="!showRmb" class="rmb-off-text" @click="toggleShowRmb">人民币</span>
      <span class=""></span>
    </div> -->
  </div>
</template>

<script>
export default {
  data () {
    return {
      showRmb: true,
      textAlign: 'text-align: center;'
    }
  },
  methods: {
    focusSearch () {
      console.log('search focus')
      this.textAlign = 'text-align: left;'
    }
  },
  created () {
    // 调用应用实例的方法获取全局数据
  }
}
</script>

<style scoped>

  .search-input {
      width: calc( 100% - 50px);
      border: 1rpx solid #384457;
      border-radius: 4px;
      padding: 3px 5px;
      font-size: 14px;
      color: white;
  }

  .self-header__container {
    display: flex;
    padding: 10px;
    border-bottom: 1rpx solid #21232A;
    justify-content: space-between;
  }

  .delete-coin img {
    width: 22px;
    height: 32px;
  }

  .self-title {
    font-size: 24px;
    font-weight: 600;
    color: #1D3F59;
  }

  .delete-coin {
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
</style>
