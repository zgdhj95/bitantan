<template>
  <div class="self-body__container">
    <ul>
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
</template>

<script>
// import axios from 'axios'
import store from '../index/store'
export default {
  data () {
    return {
      showRmb: true,
      coinList: []
    }
  },
  methods: {
    calcRmbPrice (coin) {
      let rmbPrice = 0
      if (coin.coinUnit === 'USDT') {
        rmbPrice = 6.4 * coin.price
      } else if (coin.coinUnit === 'BTC') {
        rmbPrice = 6.4 * 6865 * coin.price
      }
      console.log(' rmb price ==' + rmbPrice)
      return rmbPrice
    },
    queryData () {
      console.log('user is', store.state.userInfo)
      var that = this
      wx.request({
        url: 'https://www.coinexplorer.cn/querychainlist',
        method: 'GET',
        header: {
          'Content-Type': 'json'
        },
        success: function (res) {
          console.log(res.data)
          that.coinList = res.data
        }
      })
    }
  },
  created () {
    this.queryData()
    let self = this
    setInterval(() => {
      self.queryData()
    }, 5000)
  }
}
</script>

<style scoped>
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
</style>
