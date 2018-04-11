// https://vuex.vuejs.org/zh-cn/intro.html
// make sure to call Vue.use(Vuex) if using a module system
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    userInfo: {},
    openid: '',
    coinList: []
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
    }
  }
})

export default store
