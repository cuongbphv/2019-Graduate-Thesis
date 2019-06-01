<template>
  <div class="search-container">
    <main>
      <el-row>
        <el-col class="left" :sm="6" :md="5" :lg="5">
          <h3><i class="el-icon-s-operation" />&nbsp; Bộ lọc tìm kiếm</h3>
          <div class="search-group">
            <h4>Tìm theo danh mục</h4>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="Option A" />
              <el-checkbox label="Option B" />
              <el-checkbox label="Option C" />
            </el-checkbox-group>
            <el-button type="default">Thêm <i class="el-icon-arrow-down" /></el-button>
          </div>
          <div class="search-group">
            <h4>Tìm theo danh mục</h4>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="Option A" />
              <el-checkbox label="Option B" />
              <el-checkbox label="Option C" />
            </el-checkbox-group>
            <el-button type="default">Thêm <i class="el-icon-arrow-down" /></el-button>
          </div>
          <div class="search-group">
            <h4>Tìm theo danh mục</h4>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="Option A" />
              <el-checkbox label="Option B" />
              <el-checkbox label="Option C" />
            </el-checkbox-group>
            <el-button type="default">Thêm <i class="el-icon-arrow-down" /></el-button>
          </div>
          <div class="search-group">
            <h4>Tìm theo giá</h4>
            <el-row style="padding-bottom: 15px">
              <el-col :md="10" :lg="10">
                <money
                  v-model="searchQuery.minPrice"
                  v-bind="money"
                />
              </el-col>
              <el-col :md="4" :lg="4" class="text-center" style="margin-top: 10px">
                -
              </el-col>
              <el-col :md="10" :lg="10">
                <money
                  v-model="searchQuery.maxPrice"
                  v-bind="money"
                />
              </el-col>
            </el-row>
          </div>
          <div class="button-group">
            <el-button type="warning" size="medium">Đặt lại</el-button>
            <el-button type="primary" size="medium">Tìm kiếm</el-button>
          </div>
        </el-col>
        <el-col class="right" :sm="18" :md="19" :lg="19">
          <el-row>
            <el-col :md="16" :lg="16">
              <h4>20 result for keyword "adidas"</h4>
            </el-col>
            <el-col :md="8" :lg="8" style="text-align: right">
              <el-dropdown split-button type="default" style="text-align: center;">
                Sort
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>Action 1</el-dropdown-item>
                  <el-dropdown-item>Action 2</el-dropdown-item>
                  <el-dropdown-item>Action 3</el-dropdown-item>
                  <el-dropdown-item>Action 4</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>
          </el-row>

          <section class="content">
            <div v-for="o in 10" :key="o">
              <search-item />
            </div>
          </section>

          <pagination
            v-show="searchQuery.total > 0"
            :total="searchQuery.total"
            :page.sync="searchQuery.page"
            :limit.sync="searchQuery.limit"
            @pagination="handleSearch"
          />
        </el-col>
      </el-row>
    </main>
  </div>
</template>

<script>
import { Money } from 'v-money'
import { SearchItem } from '@/components/Advertising'
import Pagination from '@/components/Pagination'
export default {
  name: 'SearchPage',
  components: {
    SearchItem,
    Money,
    Pagination
  },
  data() {
    return {
      searchQuery: {
        minPrice: 0,
        maxPrice: 0,
        page: 1,
        limit: 20,
        total: 30
      },
      checkList: ['Option A'],
      money: {
        decimal: '.',
        thousands: ',',
        prefix: '',
        suffix: '',
        precision: 0,
        masked: false
      }
    }
  },
  methods: {
    handleSearch() {
    }
  }
}
</script>

<style lang="scss" scoped>
  .search-container {
    margin: 10px 15px;
    padding: 0 !important;
    background-color: #fff;
    box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
    main {
      padding: 30px;
      h3 {
        font-weight: 600;
        margin-bottom: 10px;
      }
      .left {
        .search-group {
          border-bottom: 1px solid rgba(0,0,0,.09);
          margin: 10px 0;
          padding: 5px 0 5px 23px;
          h4 {
            margin-bottom: 5px;
          }
          a {
            padding-left: 25px;
            color: #606266;
            &:hover {
              color: #409EFF;
            }
          }
          /deep/ .el-button--default {
            border: none;
            background: none;
            span {
              padding-left: 5px;
            }
          }
        }
        .button-group {
          text-align: center;
          .el-button {
            margin-bottom: 5px;
            margin-left: 0;
          }
        }
        /deep/ .el-checkbox-group {
          .el-checkbox {
            display: flex;
            padding: 5px 0;
          }
        }
      }

      .right {
        padding-left: 3rem;

        h4 {
          margin-top: 15px;
          padding-left: 1em;
          font-weight: 600;
        }

        .content {
          margin: 15px auto;
        }
      }
    }
  }
</style>
