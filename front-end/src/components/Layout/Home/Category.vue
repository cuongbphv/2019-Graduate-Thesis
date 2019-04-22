<template>
  <el-container>
    <section class="home-feature-block">
      <div class="container">
        <div class="home-feature-block__title">Rao vặt vui, mua may bán đắt cùng SmartMarket</div>
        <h1 class="home-feature-block__mkt-title">Mua hàng trên amazon ship về Việt Nam - Fado</h1>

        <div class="home-feature-block__benefit-panel-wrap">
          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Nhà đất</div>
              <div class="home-feature-block__benefit-panel__desc">
                Nhà đất, nhà ở, nhà trọ,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

          <div class="home-feature-block__benefit-panel-wrap__col">
            <div class="home-feature-block__benefit-panel">
              <div class="home-feature-block__benefit-panel__title">Xe cộ</div>
              <div class="home-feature-block__benefit-panel__desc">
                Ô tô, xe máy, xe đạp, xe đạp điện,...
              </div>
              <svg-icon class="home-feature-block__benefit-panel__icon" icon-class="message" />
            </div>
          </div>

        </div>
      </div>
    </section>
  </el-container>
</template>

<script>
export default {
  name: 'Category'
}
</script>

<style scoped>
  .home-feature-block {
    position: relative;
    margin-bottom: 40px
  }

  .home-feature-block .container {
    background: #fff;
    border-radius: 4px;
    padding: 20px
  }

  .home-feature-block__title {
    font-size: 20px;
    line-height: 26px;
    text-align: center;
    color: #ff6f00;
    margin-bottom: 15px;
    font-weight: 500
  }

  .home-feature-block__mkt-title {
    display: none
  }

  .web-home-page .home-feature-block {
    margin-top: 40px
  }

  .home-feature-block__benefit-panel-wrap {
    display: flex;
    flex-flow: row wrap;
    flex-direction: row;
    padding: 0;
  }

  .home-feature-block__benefit-panel-wrap__col {
    width: 20%;
    padding: 0 10px;
    margin-bottom: 20px;
    height: 100px;
  }

  .home-feature-block__benefit-panel {
    background: #f7f7f7;
    height: 100%;
    padding: 7px 10px;
    border-radius: 4px;
    position: relative;
    overflow: hidden
  }

  .home-feature-block__benefit-panel__title {
    font-weight: 500;
    margin-bottom: 5px
  }

  .home-feature-block__benefit-panel__desc {
    font-size: 12px;
    line-height: 16px;
    height: 48px;
    overflow: hidden;
    margin-bottom: 5px;
    position: relative;
    z-index: 2
  }

  .home-feature-block__benefit-panel__icon {
    position: absolute;
    right: -2px;
    bottom: -5px;
    height: 50px !important;
    width: 50px !important;
    -webkit-transform: rotate(-35deg);
    transform: rotate(-35deg);
   color: #8c939d;
  }

  .home-feature-block__benefit-panel__view-more-btn {
    font-size: 16px;
    line-height: 22px
  }

  .home-feature-block__link-panel a {
    color: #333
  }

  .home-feature-block__link-panel a:hover {
    color: #2b9af3
  }

  .home-feature-block__link-panel a:hover {
    opacity: .8
  }
</style>
