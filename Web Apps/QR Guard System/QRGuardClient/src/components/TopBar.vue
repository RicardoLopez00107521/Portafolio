<template>
  <div class="topbar">
    <div class="user">
      <img :src="userPicture" class="user-img" alt="user-img">
      <div class="user-info">
        <p class="email-info">{{ userName }}</p>
        <p class="role-info">{{ userRoles }}</p> 
      </div>
    </div>
    <button class="hamburger" @click="toggleSidebar" ref="hamburgerButton">☰</button>
    <AsideBar v-if="isSidebarVisible" @close="toggleSidebar" />
  </div>
</template>

<script>
import AsideBar from './AsideBarMobile.vue'; 

export default {
  name: 'TopBar',
  components: {
    AsideBar
  },
  data() {
    return {
      topBarWidth: '84%', 
      userEmail: localStorage.getItem('userEmail'),
      userName: localStorage.getItem('userName'),
      userPicture: localStorage.getItem('userPicture') || '../assets/google-user.png', 
      userRoles: 'Loading roles...',
      isSidebarVisible: false
    };
  },
  methods: {
    toggleSidebar() {
      this.isSidebarVisible = !this.isSidebarVisible;
    },
    fetchUserRoles() {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('No hay token almacenado.');
        return;
      }

      fetch('https://qrguard-production.up.railway.app/api/auth/whoami', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Error al obtener los roles del usuario.');
        }
        return response.json();
      })
      .then(data => {
        if (data.data && data.data.userRoles) {
          const roles = data.data.userRoles.join(', ');
          this.userRoles = roles;
        } else {
          throw new Error('No se encontraron roles para el usuario.');
        }
      })
      .catch(error => {
        console.error('Error:', error.message);
        this.userRoles = 'Error al cargar roles';
      });
    }
  },
  created() {
    this.fetchUserRoles();
  }
};
</script>

<style scoped>
@import url('../styles.css');

.topbar {
  width: 84%;
  right: 0;
  position: fixed;
  height: 50px;
  background-color: var(--white-color); 
  text-align: center;
  padding: 4px 10px 4px 10px;
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
  gap: 5px;
  z-index: 5000;
}

.user {
  display: flex;
  flex-direction: row;
}

.user-img {
  width: 40px;
  height: 40px;
  margin-right: 5px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: end;
  justify-content: center;
}

.user-info .email-info {
  color: var(--title-color);
  font-family: var(--primary-font);
  font-size: var(--menu-items-size);
  font-weight: 600;
}

.user-info .role-info {
  color: var(--title-color);
  font-family: var(--primary-font);
  font-size: 0.7rem;
}

.hamburger {
  display: none; 
  order: -1;
  font-size: 30px; 
  font-weight: bold;
  color: var(--primary-color); 
}

@media screen and (max-width: 768px) {
  .topbar {
    width: 100%; 
    flex-direction: row; 
    justify-content: space-between;
  }

  .hamburger {
    display: block; 
  }

  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 4999;
  }
}
</style>

