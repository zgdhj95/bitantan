<template >
  <div class="container">

    <self-header>
    </self-header>

    <self-body>
    </self-body>

    <div class="item-delete">
      <!-- <img src="https://static.weixiaotong.com.cn/coin-delete.svg"/>
      <span>删除自选</span> -->
    </div>
  </div>
</template>

<script>
import selfHeader from './selfHeader'
import selfBody from './selfBody'

export default {
  data () {
    return {
      motto: '你好，世界！',
      userInfo: {}
    }
  },

  components: {
    selfHeader,
    selfBody
  },

  methods: {
    clickHandle (msg, ev) {
      console.log('clickHandle:', msg, ev)
    }
  },

  created () {
  }
}
</script>

<style scoped>
.userinfo {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.userinfo-avatar {
  width: 128rpx;
  height: 128rpx;
  margin: 20rpx;
  border-radius: 50%;
}

.userinfo-nickname {
  color: #aaa;
}

.usermotto {
  margin-top: 150px;
}

.form-control {
  display: block;
  padding: 0 12px;
  margin-bottom: 5px;
  border: 1px solid #ccc;
}

.item-delete {
  display: flex;
  border-top: 1rpx #DADADA solid;
  padding: 18px 0px;
  font-size: 18px;
  color: red;
  width: 100%;
  justify-content: center;
}

.item-delete img {
  width: 16px;
  height: 16px;
}

.item-delete span {
  color: #df8a7f;
  font-size: 14px;
  padding-left: 5px;
  margin-top: -1px;
}

</style>
