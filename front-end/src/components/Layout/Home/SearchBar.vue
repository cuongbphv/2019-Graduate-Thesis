<template>
  <div class="search-bar">
    <input type="text" class="textbox" placeholder="Search">
    <button class="button"><svg-icon icon-class="search" /></button>
  </div>
</template>

<script>
export default {
  name: 'SearchBar'
}
</script>

<style lang="scss" scoped>
.search-bar {
  margin-top: 3px;
  outline: 0;
  float: left;
  -webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
  -webkit-border-radius: 4px;
  border-radius: 4px;
  .textbox {
    outline: 0;
    height: 42px;
    width: 500px;
    line-height: 42px;
    padding: 0 16px;
    cursor: pointer;
    background-color: rgba(255, 255, 255, 0.8);
    color: #212121;
    border: 0;
    float: left;
    -webkit-border-radius: 4px 0 0 4px;
    border-radius: 4px 0 0 4px;
    &:focus {
      outline: 0;
      background-color: #FFF;
    }
  }
  .button {
    outline: 0;
    background-color: rgba(38, 50, 56, 0.8);
    float: left;
    height: 42px;
    width: 42px;
    cursor: pointer;
    text-align: center;
    line-height: 42px;
    border: 0;
    color: #FFF;
    font-size: 16px;
    text-rendering: auto;
    text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
    -webkit-transition: background-color .4s ease;
    transition: background-color .4s ease;
    -webkit-border-radius: 0 4px 4px 0;
    border-radius: 0 4px 4px 0;
    &:hover {
      background-color: rgba(0, 150, 136, 0.8);
    }
  }
}
</style>
