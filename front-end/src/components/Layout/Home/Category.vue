<template>
  <section class="category">
    <div class="container">
      <div class="category-title">Rao vặt vui, mua may bán đắt cùng BeeMarket</div>
      <!--<div class="category-area">-->
      <!--<div class="category-col">-->
      <!--&lt;!&ndash;<div class="category-col-panel">&ndash;&gt;-->
      <!--&lt;!&ndash;<div class="category-col-title">Nhà đất</div>&ndash;&gt;-->
      <!--&lt;!&ndash;<div class="category-col-desc">&ndash;&gt;-->
      <!--&lt;!&ndash;Nhà đất, nhà ở, nhà trọ,...&ndash;&gt;-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
      <!--&lt;!&ndash;<svg-icon class="category-col-icon" icon-class="message" />&ndash;&gt;-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
      <!--</div>-->
      <!--</div>-->
      <el-row>
        <el-col
          v-for="cat in listCategory"
          :key="cat.id"
          :xs="12"
          :sm="8"
          :md="6"
          :lg="4"
        >
          <el-tooltip :content="cat.description || defaultDescription" placement="top" effect="light">
            <a class="category-item">
              <img
                :src="cat.image || defaultCatImg"
                :alt="cat.slug"
                class="category-img"
              >
              <p>{{ cat.name }}</p>
            </a>
          </el-tooltip>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'Category',
  data() {
    return {
      listQuery: {
        pageNumber: 1,
        pageSize: 100,
        ascSort: true,
        sortKey: 1,
        searchKey: ''
      },
      defaultCatImg: 'https://kwmv.org/wp-content/uploads/2018/06/hinh-anh-be-gai-de-thuong-xinh-dang-yeu-1.jpg',
      defaultDescription: this.$t('label.default_cate_des')
    }
  },
  computed: {
    ...mapGetters('category', ['listCategory'])
  },
  created() {
    this.getListCategory()
  },
  methods: {
    ...mapActions('category', ['loadListPagingCategory', 'deleteCategoryByIds']),
    getListCategory() {
      this.loadListPagingCategory(this.listQuery)
    }
  }
}
</script>

<style lang="scss"  scoped>
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
    font-weight: 500;
  }

  .category-item{
    margin-bottom: 1.5rem;
    display: block;
    text-align: center;

    &:hover{
      color: blue;
      cursor: pointer;
      opacity: 0.7;
    }

    &:first-child{
      margin-top: 2rem;
    }

    p{
      margin-top: 0.5rem;
    }

    .category-img {
      width: 100px;
      height: 100px;
      border-radius: 10%;
    }
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
