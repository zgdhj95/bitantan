<template>
  <div class="self__container">
    <div class="self-header__container">
      <input type="text" placeholder="请输入要添加的币种名称" v-model="searchText" :style="textAlign" class="search-input" @focus="focusSearch" @blur="blurFocus"/>
      <div class="delete-coin">
        <img src="https://static.weixiaotong.com.cn/ico_delete.svg" />
      </div>
    </div>
    <div class="search-chain__container" v-show="onSearchFocus">
      <ul v-show="onSearchFocus">
        <li class="search-item__container" v-for="(coin, index) in searchList">
          <div class="search-item-left">
            <div class="search-chain__icon">
              <img class="market-icon" :src="coin.icon"/>
            </div>
            <div class="search-chain__name">
              <span class="coin-name"> {{coin.coinName}}/{{coin.coinUnit}}
              </span>
            </div>

            <span class="search-chain__market">{{coin.marketTitle}}</span>
          </div>
          <div class="search-item-right">
            <span class="search-selected" v-show="coin.selected">已添加</span>
            <img class="search-unselected" src="http://sqxystatic.weixiaotong.com.cn/cc-plus-square.png" v-show="!coin.selected"></img>
          </div>
        </li>
      </ul>
    </div>
    <div class="self-body__container" v-show="!onSearchFocus">
      <ul v-show="!onSearchFocus">
        <li class="self-item__container" v-for="(coin, index) in coinList">
          <div class="item-left">
            <div class="market">
              <img class="market-icon" :src="coin.icon"/>
            </div>
            <div class="coin-name-unit">
              <span class="coin-name"> {{coin.coinName}}/{{coin.coinUnit}}
              </span>
              <span class="coin-unit">{{coin.marketTitle}}</span>
            </div>
          </div>
          <div class="coin-price">
            <span :class="'coin-price-rmb' + ' ' + coin.result">￥{{coin.priceRmb}}</span>
            <span class="coin-price-real">{{coin.unitStr}}{{coin.price}}</span>
          </div>
          <div :class="'coin-pricerate' + ' ' + ' rate' + coin.result">
            <span >{{coin.priceRate}}</span>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
// import axios from 'axios'
import store from '../index/store'
export default {
  data () {
    return {
      searchText: '',
      onSearchFocus: false,
      textAlign: 'text-align: center;',
      searchList: [
        {coinName: 'GNX', coinUnit: 'BTC', marketTitle: 'huobi', icon: 'https://static.feixiaohao.com/coin/9ee8ca52bc8b2185ec62d15a57dd336_mid.png', selected: true},
        {coinName: 'ETH', coinUnit: 'BTC', marketTitle: '币安', icon: 'https://static.feixiaohao.com/coin/20171221/cfc2e32f4ddd4ad8be3057eedc0ec758_2.png', selected: false},
        {coinName: 'ETH', coinUnit: 'USDT', marketTitle: 'huobi', icon: 'https://static.feixiaohao.com/coin/20170819/63639ab8b44143fcbcfc96a1cb9b1bfd_2.png', selected: false}
      ]
    }
  },
  computed: {
    coinList () {
      return store.state.coinList
    }
  },
  methods: {
    blurFocus () {
      if (this.searchText === '') {
        this.onSearchFocus = false
        this.textAlign = 'text-align: center;'
      }
    },
    focusSearch () {
      console.log('search focus')
      this.onSearchFocus = true
      this.textAlign = 'text-align: left;'
    }
  },
  created () {
  },
  watch: {
    searchText (newval) {
      const self = this
      let url = 'https://www.coinexplorer.cn/searchchain?chain=' + newval + 'openid=' + store.state.openid
      console.log('url is', url)
      wx.request({
        url: url,
        method: 'GET',
        header: {
          'Content-Type': 'json'
        },
        success: function (res) {
          console.log(res.data)
          self.searchList = res.data
        }
      })
    }
  }
}
</script>

<style scoped>

  .self__container {
    display: flex;
    flex-direction: column;
  }

  .self-body__container {
    display: flex;
    flex-direction: column;
    margin: 10px;
  }

  .self-item__container {
    padding: 0px 5px;
    display: flex;
    justify-content: space-between;
    height: 60px;
    align-items: center;
  }

  .search-item__container {
    padding: 0px 5px;
    display: flex;
    justify-content: space-between;
    height: 40px;
    align-items: center;
  }

  .item-left {
    display: flex;
    width: 140px;
  }

  .coin-name {
    margin-top: -3px;
    font-size: 16px;
    font-weight: 600;
    color: #fff;
    padding-left: 5px;
  }

  .coin-unit {
    font-size: 12px;
    color: #979797;
    padding-left: 5px;
  }

  .coin-price {
    font-size: 18px;
    font-weight: 700;
    padding-bottom: 5px;
    width: calc( 100% -  240px);
    text-align: right;
    display: flex;
    flex-direction: column;
  }

  .coin-price-real {
    font-size: 12px;
    color: #979797;
    padding-left: 5px;
    margin-top: -3px;
  }

  .coin-pricerate {
    border-radius: 2px;
    padding: 5px 2px;
    font-size: 16px;
    font-weight: 600;
    color: white;
    width: 80px;
    text-align: center;
  }

  .market {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 24px;
  }

  .market-icon {
     height: 18px;
     width: 18px;
  }

  .market span {
    padding-top: 5px;
    font-size: 12px;
    color: #6C6C6C;
  }

  .price-up {
    color: #4C9E62;
  }

  .price-down {
    color: #E36B42;
  }

  .search-item-left {
    display: flex;
    width: 150px;
    font-size: 14px;
    padding-left: 10px;
  }
  .search-item-right {
    color: white;
    padding-right: 15px;
    font-size: 14px;
  }

  .rateprice-up {
    background-color: #4C9E62;
  }

  .rateprice-down {
    background-color: #E36B42;
  }

  .coin-price-rmb {
    margin-top: 5px;
  }

  .coin-name-unit {
      display: flex;
      flex-direction: column;
  }

  .self-item__container:last-child {
    border:none
  }

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

  .search-chain__container {
    display: flex;
    flex-direction: column;
    padding-top: 15px;
  }

  .search-selected {
    font-size: 13px;
    color: #546b8e;
  }

  .search-unselected {
    width: 20px;
    height: 20px;
  }

  .search-chain__icon {
    padding-right: 5px;
  }

  .search-chain__name {
    padding: 0px 5px;
  }

  .search-chain__market {
    padding-top: 3px;
    padding-left: 5px;
    font-size: 13px;
    color: #80807a;
  }
</style>
