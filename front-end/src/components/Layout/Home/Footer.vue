<template>
  <footer class="footer-distributed">
    <div class="footer-left">
      <h3>Bee<span>Market</span></h3>
      <p class="footer-links">
        <router-link to="home">{{ $t('footer.home') }}</router-link>
        <router-link to="home">{{ $t('footer.about') }}</router-link>
        <router-link to="home">{{ $t('footer.contact') }}</router-link>
        <router-link to="home">{{ $t('footer.faq') }}</router-link>
      </p>
      <p class="footer-company-name">{{ $t('footer.copyright') }} &copy; 2019</p>
    </div>

    <div class="footer-center">
      <div>
        <i class="el-icon-location" />
        <p><span>Nguyen Van Cong, Phuong 3, Go Vap</span> Ho Chi Minh, Viet Nam</p>
      </div>
      <div>
        <i class="el-icon-phone" />
        <p>+84 357 152 791</p>
      </div>
      <div>
        <i class="el-icon-message" />
        <p><a href="mailto:support@company.com">cuongbphv@gmail.com</a></p>
      </div>
    </div>

    <div class="footer-right">
      <p class="footer-company-about">
        <span>{{ $t('footer.title') }}</span>
        {{ $t('footer.description') }}
      </p>
      <div class="footer-icons">
        <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'facebook' }" size="2x" />
        <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'linkedin' }" size="2x" />
        <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'github' }" size="2x" />
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style lang="scss" scoped>
  .footer-distributed {
    background-color: #292c2f;
    box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.12);
    box-sizing: border-box;
    width: 100%;
    text-align: left;
    font: bold 16px sans-serif;
    padding: 55px 50px;
    h3 {
      color:  #ffffff;
      font: normal 36px 'Cookie', cursive;
      margin: 0;
      span {
        color:  #ffba00;
      }
    }
    .footer-links {
      color:  #ffffff;
      margin: 20px 0 12px;
      padding: 0;
      a {
        display:inline-block;
        line-height: 1.8;
        text-decoration: none;
        color:  inherit;
        margin-right: 10px;
      }
    }
    .footer-icons {
      .svg-inline--fa {
        background-color:  #33383b;
        color: #ffffff;
        font-size: 25px;
        width: 38px;
        height: 38px;
        border-radius: 50%;
        text-align: center;
        line-height: 42px;
        margin: 5px 10px;
        vertical-align: middle;
      }
    }
    .footer-company-name{
      color:  #8f9296;
      font-size: 14px;
      font-weight: normal;
      margin: 0;
    }
    .footer-center{
      width: 35%;
      i {
        background-color: #33383b;
        color: #ffffff;
        font-size: 25px;
        width: 38px;
        height: 38px;
        border-radius: 50%;
        text-align: center;
        line-height: 42px;
        margin: 10px 15px;
        vertical-align: middle;
      }
    }
  }

  .footer-distributed .footer-left,
  .footer-distributed .footer-center,
  .footer-distributed .footer-right{
    display: inline-block;
    vertical-align: top;
  }

  /* Footer left */

  .footer-distributed .footer-left{
    width: 40%;
  }

  .footer-distributed .footer-center p{
    display: inline-block;
    color: #ffffff;
    vertical-align: middle;
    margin:0;
  }

  .footer-distributed .footer-center p span{
    display:block;
    font-weight: normal;
    font-size:14px;
    line-height:2;
  }

  .footer-distributed .footer-center p a{
    color:  #5383d3;
    text-decoration: none;;
  }

  /* Footer Right */

  .footer-distributed .footer-right{
    width: 20%;
  }

  .footer-distributed .footer-company-about{
    line-height: 20px;
    color:  #92999f;
    font-size: 13px;
    font-weight: normal;
    margin: 0;
  }

  .footer-distributed .footer-company-about span{
    display: block;
    color:  #ffffff;
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 20px;
  }

  .footer-distributed .footer-icons{
    margin-top: 25px;
  }

  .footer-distributed .footer-icons a{
    display: inline-block;
    width: 35px;
    height: 35px;
    cursor: pointer;
    background-color:  #33383b;
    border-radius: 2px;

    font-size: 20px;
    color: #ffffff;
    text-align: center;
    line-height: 35px;

    margin-right: 3px;
    margin-bottom: 5px;
  }

  /* If you don't want the footer to be responsive, remove these media queries */

  @media (max-width: 880px) {

    .footer-distributed{
      font: bold 14px sans-serif;
    }

    .footer-distributed .footer-left,
    .footer-distributed .footer-center,
    .footer-distributed .footer-right{
      display: block;
      width: 100%;
      margin-bottom: 40px;
      text-align: center;
    }

    .footer-distributed .footer-center i{
      margin-left: 0;
    }

  }

</style>
