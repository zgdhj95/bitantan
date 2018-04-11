<template>
  <div class="self__container">
    <div class="self-header__container">
      <input type="text" placeholder="请输入要添加的币种名称" :style="textAlign" class="search-input" @focus="focusSearch" @blur="blurFocus"/>
      <div class="delete-coin">
        <img src="https://static.weixiaotong.com.cn/ico_delete.svg" />
      </div>
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
      onSearchFocus: false,
      textAlign: 'text-align: center;'
    }
  },
  computed: {
    coinList () {
      return store.state.coinList
    }
  },
  methods: {
    blurFocus () {
      this.onSearchFocus = false
      this.textAlign = 'text-align: center;'
    },
    focusSearch () {
      console.log('search focus')
      this.onSearchFocus = true
      this.textAlign = 'text-align: left;'
    }
  },
  created () {
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
</style>
