<template>
  <div class="self-body__container">
    <ul>
      <li class="self-item__container" v-for="coin in coinList" :key="coin">
        <div class="item-left">
          <div class="market">
            <img class="market-icon" :src="coin.icon"/>
            <!-- <span>{{coin.marketTitle}}</span> -->
          </div>
          <div class="coin-name-unit">
            <span class="coin-name"> {{coin.coinName}}
            </span>
            <span class="coin-unit">{{coin.coinUnit}}</span>
          </div>
        </div>
        <span :class="'coin-price ' + ' '+ coin.result">{{coin.price}}</span>
        <div :class="'coin-pricerate' + ' ' + ' rate' + coin.result">
          <span >{{coin.priceRate}}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data () {
    return {
      showRmb: true,
      coinList: [
      ]
    }
  },
  methods: {
    toggleShowRmb () {
      this.showRmb = !this.showRmb
    }
  },
  created () {
    // 调用应用实例的方法获取全局数据
    axios.get('http://123.207.241.107/querychainlist')
      .then(response => {
        console.log('resp = ', response.data)
        console.log('this is', this)
        this.coinList = response.data
      })
      .catch(function (error) {
        console.log(error)
      })
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
    border-bottom: 1rpx solid #bababa;
  }

  .item-left {
    display: flex;
  }

  .coin-name {
    margin-top: -3px;
    font-size: 16px;
    font-weight: 600;
    color: white;
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
    width: calc( 100% -  200px);
    text-align: right;
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

  .coin-name-unit {
      display: flex;
      flex-direction: column;
  }

  .self-item__container:last-child {
    border:none
  }

  ul {
    padding: 0px;
  }
</style>
