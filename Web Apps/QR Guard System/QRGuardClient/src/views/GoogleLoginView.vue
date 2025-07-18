<template>
  <div class="google-login">
    <div v-if="loggedIn" class="user-info">
      <button @click="logout" class="logout-button">Cerrar sesión</button>
      <h2>Nombre: {{ user.name }}</h2>
      <h2>Email: {{ user.email }}</h2>
      <img :src="user.picture" alt="profile picture">
    </div>
    <div v-else>
      <GoogleLogin :callback="onGoogleLoginSuccess" prompt auto-login/>
    </div>
  </div>
</template>

<script>
import { decodeCredential } from 'vue3-google-login';

export default {
  data() {
    return {
      loggedIn: false,
      user: null,
    };
  },
  methods: {
    onGoogleLoginSuccess(response) {
      console.log('Logged in successfully');
      console.log('Response:', response);
      this.user = decodeCredential(response.credential);
      this.loggedIn = true;
    },
    logout() {
      // Aquí podrías añadir la lógica para cerrar sesión si es necesario
      this.loggedIn = false;
      this.user = null;
    },
  },
};
</script>

<style scoped>
.google-login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.user-info {
  text-align: center;
}

.logout-button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 20px;
}

.logout-button:hover {
  background-color: #dd3333;
}

img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin-top: 20px;
}

h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

@media (max-width: 768px) {
  img {
    width: 100px;
    height: 100px;
  }
}
</style>

