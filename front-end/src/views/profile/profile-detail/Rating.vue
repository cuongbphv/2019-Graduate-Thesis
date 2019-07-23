<template>
  <div class="rating-container">
    <el-row>
      <el-col :md="{span: 11, offset: 1}">
        <div class="rating-block">
          <h4>Đánh giá trung bình</h4>
          <h2 class="bold padding-bottom-7"> {{ profile.ratingAverage.toFixed(1) }} <small>/ 5</small></h2>
          <el-rate
            v-model="profile.ratingAverage"
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
            <el-progress style="width: 85%; display: inline-block;" :percentage="ratingBreakDown.five" color="#6f7ad3" :show-text="false" />
            <span style="margin-left:10px;">{{ (ratingBreakDown.five * ratingBreakDown.total / 100) || 0 }}</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>4 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="ratingBreakDown.four" color="#1989fa" :show-text="false" />
            <span style="margin-left:10px;">{{ (ratingBreakDown.four * ratingBreakDown.total / 100) || 0 }}</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>3 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="ratingBreakDown.three" color="#5cb87a" :show-text="false" />
            <span style="margin-left:10px;">{{ (ratingBreakDown.three * ratingBreakDown.total / 100) || 0 }}</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>2 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="ratingBreakDown.two" color="#e6a23c" :show-text="false" />
            <span style="margin-left:10px;">{{ (ratingBreakDown.two * ratingBreakDown.total / 100) || 0 }}</span>
          </el-row>
          <el-row style="margin-bottom: 3px">
            <span>1 <i class="el-icon-star-on" /></span>
            <el-progress style="width: 85%; display: inline-block;" :percentage="ratingBreakDown.one" color="#f56c6c" :show-text="false" />
            <span style="margin-left:10px;">{{ (ratingBreakDown.one * ratingBreakDown.total / 100) || 0 }}</span>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <hr>
    <el-row v-if="!!userId && (profileUserId !== userId)">
      <el-col :md="{span: 23, offset: 1}">
        <div class="review-block">
          <el-form label-position="left" label-width="100px" :model="ratingModel" :disabled="formDisable">
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
            <el-form-item v-if="!formDisable">
              <el-button v-if="!ratingModel.senderId" type="success" style="float:right" @click="handleCreateRating">Đánh giá</el-button>
              <el-button v-if="ratingModel.senderId" type="success" style="float:right" :disabled="false" @click="handleUpdateRating">Cập nhật</el-button>
            </el-form-item>
          </el-form>
          <el-row v-if="formDisable">
            <el-button type="warning" style="float:right" @click="formDisable = false">Chỉnh sửa</el-button>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :md="{span: 23, offset: 1}">
        <div class="review-block">
          <template v-for="rating in ratings">
            <el-row :key="rating.id">
              <el-col :md="6">
                <img :src="rating.sender.avatarUrl" class="img-rounded">
                <div class="review-block-name"><a href="#"><b>{{ (rating.sender.lastName || '') + ' ' + rating.sender.firstName }}</b></a></div>
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
      profileUserId: this.$route.params.id,
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
      },
      ratingBreakDown: {
        one: 0,
        two: 0,
        three: 0,
        four: 0,
        five: 0,
        total: 0
      },
      ratingSize: 10,
      formDisable: false
    }
  },
  computed: {
    ...mapGetters('rating', ['ratings']),
    ...mapGetters('profile', ['userId', 'profile'])
  },
  watch: {
    profile: function(newVal) {
      // this.value = newVal.ratingAverage / 5
    }
  },
  mounted() {
    this.handleGetRating()
    this.getCurrentUserRating()
  },
  methods: {
    ...mapActions('rating', ['createRating', 'getRating', 'getAllRating', 'updateRating', 'getRatingForUser', 'deleteRating']),
    formatDate(rawDate) {
      return date.formatDate(rawDate)
    },
    handleGetRating() {
      this.getRatingForUser({ userId: this.profileUserId, size: this.ratingSize })
    },
    getCurrentUserRating() {
      this.handleRatingStats()
      this.getRating(this.profileUserId).then((res) => {
        this.ratingModel.id = res.id
        this.ratingModel.comment = res.comment
        this.ratingModel.value = res.value
        this.ratingModel.title = res.title
        this.ratingModel.senderId = res.senderId
        this.formDisable = true
      })
    },
    handleRatingStats() {
      this.getAllRating(this.profileUserId).then((res) => {
        if (res) {
          const total = res.length
          this.ratingBreakDown.total = total
          this.ratingBreakDown.one = (res.filter((ele) => {
            return ele.value === 1
          }).length) * 100 / total
          this.ratingBreakDown.two = (res.filter((ele) => {
            return ele.value === 2
          }).length) * 100 / total
          this.ratingBreakDown.three = (res.filter((ele) => {
            return ele.value === 3
          }).length) * 100 / total
          this.ratingBreakDown.four = (res.filter((ele) => {
            return ele.value === 4
          }).length) * 100 / total
          this.ratingBreakDown.five = (res.filter((ele) => {
            return ele.value === 5
          }).length) * 100 / total
        }
      })
    },
    handleCreateRating() {
      this.ratingModel.senderId = this.userId
      this.ratingModel.recipientId = this.profileUserId
      this.createRating(this.ratingModel).then(() => {
        this.ratingModel = Object.assign({},
          {
            value: 0,
            title: '',
            comment: ''
          })
        this.handleGetRating()
        this.getCurrentUserRating()
      })
    },
    handleUpdateRating() {
      this.ratingModel.recipientId = this.profileUserId
      this.updateRating(this.ratingModel).then(() => {
        this.handleGetRating()
        this.getCurrentUserRating()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
  body {
    padding-top: 70px;
  }
  .rating-container{
    width: 100%;
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
