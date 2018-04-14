<template>
  <div class="container">

    <self-page ></self-page>
  </div>
</template>

<script>
import selfPage from '../self'
import store from './store'

export default {
  data () {
    return {
      motto: '你好，世界！',
      userInfo: {}
    }
  },

  components: {
    selfPage
  },

  methods: {
    queryData () {
      let url = 'https://www.coinexplorer.cn/querychainlist?openid=' + store.state.openid
      console.log('url is', url)
      wx.request({
        url: url,
        method: 'GET',
        header: {
          'Content-Type': 'json'
        },
        success: function (res) {
          console.log(res.data)
          store.commit('saveCoinList', res.data.coinList)
          store.commit('saveAsset', res.data.asset)
        }
      })
    },
    getUserInfo () {
      const self = this
      // 调用登录接口
      wx.login({
        success: (res) => {
          console.log(' res.cod=', res)
          wx.request({
            url: 'https://www.coinexplorer.cn/queryuser?code=' + res.code,
            method: 'GET',
            header: {
              'Content-Type': 'json'
            },
            success: function (res) {
              console.log(res.data)
              store.commit('saveOpenid', res.data.openid)
              self.queryData()
              setInterval(() => {
                self.queryData()
              }, 5000)
            }
          })
          wx.getUserInfo({
            success: (res) => {
              this.userInfo = res.userInfo
              console.log('user is', res)
              store.commit('saveUser', res.userInfo)
            }
          })
        }
      })
    },
    clickHandle (msg, ev) {
      console.log('clickHandle:', msg, ev)
    }
  },

  created () {
    // 调用应用实例的方法获取全局数据
    this.getUserInfo()
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
</style>
