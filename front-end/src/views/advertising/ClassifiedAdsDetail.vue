<template>
  <div class="classified-ads-detail-container">
    <header>
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">Trang chủ</el-breadcrumb-item>
        <el-breadcrumb-item>promotion management</el-breadcrumb-item>
        <el-breadcrumb-item>promotion list</el-breadcrumb-item>
        <el-breadcrumb-item>promotion detail</el-breadcrumb-item>
      </el-breadcrumb>
    </header>
    <main>
      <article>
        <el-row>
          <el-col class="carousel-left" :sm="8" :md="6">
            <el-upload action="" :file-list="classifiedAds.images" list-type="picture-card" />
          </el-col>

          <el-col class="carousel-right" :sm="16" :md="18">
            <el-carousel :interval="5000" arrow="always">
              <el-carousel-item v-for="item in classifiedAds.images" :key="item.uid">
                <img :src="item.url" alt="No image">
              </el-carousel-item>
            </el-carousel>
          </el-col>
        </el-row>
        <div>
          <h2><strong>ae qwe qw eqw e qwe qw e qw eqw e qwe qw eqw e qwe qw eqw eqw eqw e qwe qw e</strong></h2>
        </div>
      </article>
      <aside>aside</aside>
    </main>
    <footer>footer</footer>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'ClassifiedAdsDetail',
  data() {
    return {
      classifiedAdsId: ''
    }
  },
  computed: {
    ...mapState('advertising', ['classifiedAds'])
  },
  created() {
    this.classifiedAdsId = this.$route.params.id
    this.getClassifiedAdsDetail(this.classifiedAdsId).then(() => {
      console.info(this.classifiedAds)
    })
  },
  methods: {
    ...mapActions('advertising', ['getClassifiedAdsDetail'])
  }
}
</script>

<style lang="scss" scoped>
.classified-ads-detail-container {
  margin: 10px 15px;
  padding: 10px 20px;
  background-color: #fff;
  box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
  main {
    min-height: 50rem;
    margin: 0;
    padding: 0;
    display: flex;
    flex-flow: row;
  }

  main > article {
    margin: 4px;
    padding: 5px;
    background: rgba(255, 255, 255, 0.9);
    flex: 3 1 60%;
    order: 2;
    .carousel-left {
      text-align: center;
      height: 450px;
      overflow-y: auto;

      /deep/ .el-upload-list__item {
        border-radius: 0;

        .el-upload-list__item-status-label {
          display: none;
        }
        .el-icon-close-tip {
          display: none;
        }
        .el-icon-close {
          display: none;
        }
        .el-upload-list__item-actions{
          display: none;
        }
      }
      /deep/ .el-upload--picture-card {
        display: none;
      }
    }
    .carousel-right {
      /deep/ .el-carousel {
        height: 450px;
        .el-carousel__container {
          height: 100% !important;
        }
      }
      img {
        width: 100%;
        height: 450px;
      }
    }
  }

  /*main > nav {*/
  /*  margin: 0.25rem;*/
  /*  padding: 0.312rem;*/
  /*  border: 0.187rem solid rgba(0, 0, 0, 0.6);*/
  /*  border-radius: 0.625rem;*/
  /*  background: rgba(255, 255, 255, 0.9);*/
  /*  flex: 1 6 20%;*/
  /*  order: 1;*/
  /*}*/

  main > aside {
    margin: 0.25rem;
    padding: 0.312rem;
    border: 0.187rem solid rgba(0, 0, 0, 0.6);
    border-radius: 0.625rem;
    background: rgba(255, 255, 255, 0.9);
    flex: 1 6 20%;
    order: 3;
  }

  header {
    display: block;
    margin: 0.25rem;
    padding: 0.312rem;
    min-height: 2rem;
    background: rgba(255, 255, 255, 0.9);
  }

  footer {
    display: block;
    margin: 0.25rem;
    padding: 0.312rem;
    min-height: 6.25rem;
    border: 0.187rem solid rgba(0, 0, 0, 0.6);
    border-radius: 0.625rem;
    background: rgba(255, 255, 255, 0.9);
  }

  @media all and (max-width: 640px) {
    main {
      flex-direction: column;
    }

    main > article,
    main > nav,
    main > aside {
      order: 0;
    }

    main > nav,
    main > aside,
    header,
    footer {
      min-height: 3.125rem;
      max-height: 3.125rem;
    }
  }
}
</style>
