<template>
  <div id="app">
    <RouterView></RouterView>
  </div>
</template>

<script>
import Login from './views/LoginView.vue'

export default {
  name: 'App',
  components: {
    Login
  }
}
</script>

<style>
@import url('./styles.css');

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

.login{
  width: 10px;
}

</style>