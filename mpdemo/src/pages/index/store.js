// https://vuex.vuejs.org/zh-cn/intro.html
// make sure to call Vue.use(Vuex) if using a module system
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    userInfo: {},
    openid: '',
    coinList: [],
    assetBtc: 0,
    assetRmb: 0
  },
  mutations: {
    saveCoinList: (state, coinList) => {
      state.coinList = coinList
    },
    saveUser: (state, userInfo) => {
      state.userInfo = userInfo
    },
    saveOpenid: (state, openid) => {
      state.openid = openid
    },
    saveAsset: (state, asset) => {
      let assets = asset.split('_')
      state.assetBtc = assets[0]
      state.assetRmb = assets[1]
    }
  }
})

export default store
