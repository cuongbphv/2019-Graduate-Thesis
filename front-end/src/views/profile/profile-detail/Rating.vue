<template>
  <div class="container">
    <el-row>
      <el-col :md="{span: 11, offset: 1}">
        <div class="rating-block">
          <h4>Đánh giá trung bình</h4>
          <h2 class="bold padding-bottom-7">4.3 <small>/ 5</small></h2>
          <el-rate
            v-model="value"
            disabled
            text-color="#ff9900"
            score-template="{value} / 5"
          />
        </div>
      </el-col>
      <el-col :md="{span: 11, offset: 1}">
        <div class="rating-block">
          <el-row style="margin-bottom: 3px">
            <span>5 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="100" color="#6f7ad3" :show-text="false" />
            <span style="margin-left:10px;">1</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>4 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="80" color="#1989fa" :show-text="false" />
            <span style="margin-left:10px;">1</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>3 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="60" color="#5cb87a" :show-text="false" />
            <span style="margin-left:10px;">1</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>2 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="40" color="#e6a23c" :show-text="false" />
            <span style="margin-left:10px;">1</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>1 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="20" color="#f56c6c" :show-text="false" />
            <span style="margin-left:10px;">1</span>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <hr>
    <el-row>
      <el-col :md="{span: 23, offset: 1}">
        <div class="review-block">
          <el-form label-position="left" label-width="100px" :model="ratingModel">
            <el-form-item label="Số sao">
              <el-rate
                v-model="ratingModel.value"
                text-color="#ff9900"
                style="margin-top: 8px"
              />
            </el-form-item>
            <el-form-item label="Tiêu đề">
              <el-input v-model="ratingModel.title" />
            </el-form-item>
            <el-form-item label="Nội dung">
              <el-input
                v-model="ratingModel.comment"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4}"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="success" style="float:right">Đánh giá</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :md="{span: 23, offset: 1}">
        <hr>
        <div class="review-block">
          <template v-for="rating in ratings">
            <el-row :key="rating.id">
              <el-col :md="6">
                <img :src="rating.sender.avatarUrl" class="img-rounded">
                <div class="review-block-name"><a href="#"><b>{{ rating.sender.lastName + ' ' + rating.sender.firstName }}</b></a></div>
                <div class="review-block-date">{{ formatDate(rating.createdDate) }}</div>
              </el-col>
              <el-col :md="18">
                <div style="width: 500px" class="review-block-rate">
                  <el-rate
                    v-model="rating.value"
                    disabled
                    text-color="#ff9900"
                  />
                </div>
                <div class="review-block-title">{{ rating.title || '' }}</div>
                <p class="review-block-description">{{ rating.comment }}</p>
              </el-col>
            </el-row>
            <hr :key="rating.id + 1">
          </template>
        </div>
      </el-col>
    </el-row>

  </div> <!-- /container -->
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import date from '@/utils/date'

export default {
  name: 'Rating',
  data() {
    return {
      userId: this.$route.params.id,
      value: 4.3,
      customColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 },
        { color: '#1989fa', percentage: 80 },
        { color: '#6f7ad3', percentage: 100 }
      ],
      ratingModel: {
        value: 0,
        title: '',
        comment: ''
      }
    }
  },
  computed: {
    ...mapGetters('rating', ['ratings'])
  },
  mounted() {
    this.getRatingForUser({ userId: this.userId, size: 10 })
  },
  methods: {
    ...mapActions('rating', ['createRating', 'getRating', 'updateRating', 'getRatingForUser', 'deleteRating']),
    formatDate(rawDate) {
      return date.formatDate(rawDate)
    }
  }
}
</script>
<style lang="scss" scoped>
  body {
    padding-top: 70px;
  }
  .btn-grey{
    background-color:#D8D8D8;
    color:#FFF;
  }
  .rating-block{
    background-color:#FAFAFA;
    border:1px solid #EFEFEF;
    padding:15px 15px 20px 15px;
    border-radius:3px;
  }
  .bold{
    font-weight:700;
  }
  .padding-bottom-7{
    padding-bottom:7px;
  }

  .review-block{
    background-color:#FAFAFA;
    border:1px solid #EFEFEF;
    padding:15px;
    border-radius:3px;
    margin-bottom:15px;
  }
  .review-block-name{
    font-size:12px;
    margin:10px 0;
  }
  .review-block-date{
    font-size:12px;
  }
  .review-block-rate{
    font-size:13px;
    margin-bottom:15px;
  }
  .review-block-title{
    font-size:15px;
    font-weight:700;
    margin-bottom:10px;
  }
  .review-block-description{
    font-size:13px;
  }
  .img-rounded{
    width: 64px;
    height: 64px;
    border-radius: 50%;
  }
</style>
