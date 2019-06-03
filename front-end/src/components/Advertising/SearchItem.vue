<template>
  <article>
    <a target="_blank" href="https://sitechop.com/posts/12-speeding-up-your-website">
      <div class="postImg">
        <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png">
      </div>
      <div class="postCont">
        <h3>Speeding Up Your Website 101</h3>
        <span>by John Doe- 23 mins ago</span>
        <h1>1.500.000 đ</h1>
        <p class="address">Ward 1 | Thu Duc District | Ho Chi Minh City</p>
        <el-rate
          v-model="value2"
          :colors="colors"
        />
        <el-row style="margin-top: 10px">
          <el-tooltip content="Bookmark" effect="dark" placement="bottom">
            <el-button type="success" icon="el-icon-paperclip" circle />
          </el-tooltip>
          <el-tooltip content="Report" effect="dark" placement="bottom">
            <el-button type="warning" icon="el-icon-lock" circle />
          </el-tooltip>
        </el-row>
      </div>
    </a>
  </article>
</template>

<script>
export default {
  name: 'SearchItem',
  props: {
    ads: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      value2: null,
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'] // same as { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }
    }
  }
}
</script>

<style lang="scss" scoped>
  article {
    margin-top: -0.063em;
    h1 {
      margin: 10px auto;
      color: #ffba00;
    }
    a {
      display: block;
      padding: 1em 2em 1em 1em;
      overflow: hidden;
      border-top: .063em solid #ccc;
      border-bottom: .063em solid #ccc;
      h3 {
        color: #606266;
        width: 75%;
      }
      p {
        width: 75%;
      }
      &:hover {
        background: #eee;
        padding: 1em 2em 1em 1em;
        h3 {
          color: #157EBF;
        }
        p {
          color: #222;
        }
      }
    }

    .postCont {
      float: right;
      width: 75%;
      line-height: 20px;
    }

    .postImg {
      float: left;
      width: 25%;
      box-sizing: border-box;
      padding-right: 2em;
      img {
        width: 100%;
      }
    }
  }

  @media only screen and (max-width: 750px) {
    #postIndex {
      width: 100%;
    }

    article a {
      padding: 2.374em 1.5em 2.5em 1.5em;
    }

    article a:hover {
      padding: 2.187em 1.5em 2.5em 1.5em;
    }
  }

  @media only screen and (max-width: 481px) {
    .postImg {
      display: none;
    }

    article a p, article a h2 {
      width: 100%;
    }
  }
</style>
