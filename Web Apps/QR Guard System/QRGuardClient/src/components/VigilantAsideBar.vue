<template>
<aside class="sidebar">
  <div class="main-info">
    <img class="logo" src="../assets/logo.png">
    <div class="links">
      <router-link to="/vigilantView">
      <div class="item-container qr-icon" :class="{ 'active-hover': $route.path === '/vigilantView' }" >
        <svg class="icons">
        <path fill="#ffffff" d="m4,11h7v-7h-7v7Zm16-7h-7v7h7v-7ZM4,20h7v-7h-7v7Zm-1,2h4v2H3c-1.654,0-3-1.346-3-3v-4h2v4c0,.551.449,1,1,1Zm19-5h2v4c0,1.654-1.346,3-3,3h-4v-2h4c.551,0,1-.449,1-1v-4Zm2-14v4h-2V3c0-.551-.449-1-1-1h-4V0h4c1.654,0,3,1.346,3,3ZM2,7H0V3C0,1.346,1.346,0,3,0h4v2H3c-.551,0-1,.449-1,1v4Zm11,10h3v3h-3v-3Zm4-1v-3h3v3h-3Zm-4-3h3v3h-3v-3Z"/>
        </svg>
        <p class="item-link">Escáner QR</p>
      </div>
      </router-link>
      <router-link to="/vigilantView/createEntryView">
      <div class="item-container home-icon" :class="{ 'active-hover': $route.path === '/vigilantView/createEntryView' }">
        <svg class="icons">
          <path fill="#ffffff" d="m24,15c0,.617-.24,1.197-.678,1.634l-2.072,2.073c-.195.195-.451.293-.707.293s-.512-.098-.707-.293c-.391-.391-.391-1.023,0-1.414l1.292-1.293h-6.128c-.553,0-1-.447-1-1s.447-1,1-1h6.128l-1.292-1.293c-.391-.391-.391-1.023,0-1.414s1.023-.391,1.414,0l2.073,2.074c.437.436.677,1.016.677,1.633Zm-10-4v-5c0-1.103-.897-2-2-2h0s0,0,0,0v17.999h1c.552,0,1-.448,1-1v-2c0-.553.447-1,1-1s1,.447,1,1v2c0,1.654-1.346,3-3,3H3c-1.654,0-3-1.346-3-3V5.621C0,3.246,1.69,1.184,4.019.718L7.216.079c1.181-.236,2.391.066,3.321.829.375.307.665.685.902,1.092h.561c2.206,0,4,1.794,4,4v5c0,.553-.447,1-1,1s-1-.447-1-1Zm-5,1.5c0-.828-.672-1.5-1.5-1.5s-1.5.672-1.5,1.5.672,1.5,1.5,1.5,1.5-.672,1.5-1.5Z"/>
        </svg>
        <p class="item-link">Entradas</p>
      </div>
      </router-link>
    </div>
  </div>
  <router-link to="/">
     <svg class="exit" xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 -960 960 960" width="48px" fill="#FFFF"><path d="M180-120q-24 0-42-18t-18-42v-600q0-24 18-42t42-18h299v60H180v600h299v60H180Zm486-185-43-43 102-102H360v-60h363L621-612l43-43 176 176-174 174Z"/>
    </svg>
  </router-link>
</aside>
</template>

<script>
export default {
  name: 'AsideBar',
  

  
};
</script>

<style scoped>
@import url('../styles.css');
.sidebar {
  position: fixed;
  left: 0;
  top: 0; 
  bottom: 0; 
  background-color: var(--primary-color); 
  width: 16%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom:20px;
}

.logo{
  margin-top: 20px;
  width: 70px;
  height: 80px;
  margin-left: auto;
  margin-right: auto;
}

.links {
  display: flex;
  width: 83%;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
  margin-top: 15px;
  gap: 10px;
}

.item-container{
  padding: 8px 25px 8px 25px;
  gap: 10px;
  width: auto;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  justify-content: start;
  align-items: center;
}

.item-container .item-link{
  color: var(--white-color);
  font-family: var(--primary-font);
  font-size: var(--menu-items-size);
  font-weight: 300;
}

.icons{
  width: 25px;
  height: 25px; 
}

.exit{
  width: 35px;
  height: 35px; 
}

.item-link {
  text-decoration: none;
}

.item-container:hover, .item-container.active-hover {
  background-color: var(--white-color);
  border-radius: 5px;
  cursor: pointer;
}

.item-container:hover .item-link, .item-container.active-hover .item-link {
  color: var(--primary-color); 
  font-weight: bold;
}

.item-container:hover .icons path, .item-container.active-hover .icons path {
  fill: var(--primary-color);
}

@media screen and (min-width: 769px) and (max-width: 1200px) {
  .item-link {
    display: none;
    
  }

  .item-container{
    justify-content: center;
  }
}

@media screen and (max-width: 768px) {
  .sidebar {
    display: none;
  }
}
</style>

