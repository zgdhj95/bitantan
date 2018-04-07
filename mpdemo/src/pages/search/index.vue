<template>
  <div class="self-header__container">
    <input ref="searchinput" type="text" placeholder="请输入要添加的币种名称" class="search-input" @change="searchChain"/>
  </div>
</template>

<script>
export default {
  data () {
    return {
      showRmb: true
    }
  },
  methods: {
    focusSearch () {
      console.log('search focus')
    }
  },
  created () {
    // 调用应用实例的方法获取全局数据
  }
}
</script>

<style scoped>

  .search-input {
      color: white;
      width: 100%;
      border: 1rpx solid #384457;
      border-radius: 4px;
      padding: 3px 5px;
      font-size: 14px;
      text-align: center;
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
