<template>
  <section class="category">
    <div class="container">
      <div class="category-title">Rao vặt vui, mua may bán đắt cùng BeeMarket</div>

      <div class="category-area">

        <div class="category-col">
          <div class="category-col-panel">
            <div class="category-col-title">Nhà đất</div>
            <div class="category-col-desc">
              Nhà đất, nhà ở, nhà trọ,...
            </div>
            <svg-icon class="category-col-icon" icon-class="message" />
          </div>
        </div>

      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'Category'
}
</script>

<style scoped>
  .category {
    position: relative;
  }

  .category .container {
    background: #fff;
    padding: 20px
  }

  .category-title {
    font-size: 20px;
    line-height: 26px;
    text-align: center;
    color: #ff6f00;
    margin-bottom: 15px;
    font-weight: 500
  }

  .category-area {
    width: 100%;
    max-width: 1240px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto;
  }

  @media only screen and (min-width: 500px) {
    .category-area {
      grid-template-columns: 1fr 1fr;
    }
  }

  @media only screen and (min-width: 850px) {
    .category-area {
      grid-template-columns: 1fr 1fr 1fr 1fr;
    }
  }

  .category-col {
    flex: 0 0 25%;
    padding: 0 5px;
    margin-bottom: 20px;
    height: 150px;
  }

  .category-col-panel {
    background: #f7f7f7;
    height: 100%;
    padding: 7px 10px;
    border-radius: 4px;
    position: relative;
    overflow: hidden
  }

  .category-col-title {
    font-weight: 500;
    margin-bottom: 5px
  }

  .category-col-desc {
    font-size: 12px;
    line-height: 16px;
    height: 48px;
    overflow: hidden;
    margin-bottom: 5px;
    position: relative;
    z-index: 2
  }

  .category-col-icon {
    position: absolute;
    right: -2px;
    bottom: -5px;
    height: 70px !important;
    width: 70px !important;
    -webkit-transform: rotate(-35deg);
    transform: rotate(-35deg);
   color: #8c939d;
  }
</style>
