<template>
  <VigilantAsideBar></VigilantAsideBar>
  <div class="container">
    <TopBar class="topbar"></TopBar>
    <div class="router">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import VigilantAsideBar from '../components/VigilantAsideBar.vue'
import TopBar from '../components/TopBar.vue'

export default {
  name: 'MainView',
  components: {
    VigilantAsideBar,
    TopBar
  }
}
</script>

<style>
@import url('../styles.css');

html, body{
  background-color: var(--bg-color);
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.container{
  display: flex;
  flex-direction: column;
  align-items: end;
  margin-left: 16%;
}

.router{
  margin-top: 50px;
  width: 100%;
}

@media screen and (max-width: 768px) {
  .container{
    margin-left: 0;
  }

}

</style>