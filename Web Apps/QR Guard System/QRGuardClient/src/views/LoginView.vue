<template>
  <div class="landing">
    <div class="title">
      <img src="../assets/logo.png" alt="google-icon">
      <p>Acceso sin demoras, seguridad sin preocupaciones</p>
      <button @click="scrollToBenefits">Conoce más</button>
    </div>
    <div class="cardL">
      <h4>Inicia sesión</h4>
      <p>Regístrate e inicia sesión con tu cuenta de Google</p>
      <button @click="handleGoogleSignIn">
        <img class="google" src="../assets/google.png" alt="google-icon">
      </button>
    </div>
  </div>
  <div class="info" id="beneficios">
      <h4>Conoce nuestros beneficios...</h4>
      <div class="benefits">
        <div class="cards">
          <h5>Acceso mediante QR</h5>
          <img src="../assets/benefit_1.png" alt="google-icon">
          <p>Accede fácilmente escaneando un código QR único.</p>
        </div>
        <div class="cards">
          <h5>Solicitud de Permisos</h5>
          <img src="../assets/benefit_2.png" alt="google-icon">
          <p>Solicita permisos de entrada con solo unos toques.</p>
        </div>
        <div class="cards">
          <h5>Gestión Centralizada</h5>
          <img src="../assets/benefit_3.png" alt="google-icon">
          <p>Administra y monitorea todos los accesos a tu hogar desde una sola plataforma.</p>
        </div>
      </div>
    </div>
  <footer>
    QRGuard
  </footer>
</template>

<script>
import { googleTokenLogin } from 'vue3-google-login';

export default {
  name: 'LoginView',
  components: {
    
  },
  data() {
    return {
      
    };
  },
  methods: {
    scrollToBenefits() {
      const benefitsSection = document.getElementById('beneficios');
      if (benefitsSection) {
        const benefitsPosition = benefitsSection.offsetTop;
        window.scrollTo({
          top: benefitsPosition,
          behavior: 'smooth'
        });
      }
    },
    handleGoogleSignIn() {
      googleTokenLogin({
        client_id: '665325527090-eppkp6kremu3sio9pk9d3bakmvh3g892.apps.googleusercontent.com',
        scope: 'openid profile email',
        prompt: 'select_account'
      })
      .then(this.handleGoogleSuccess)
      .catch(this.handleGoogleFailure);
    },
    handleGoogleSuccess(response) {
      const accessToken = response.access_token;
      
      // Fetch user information using the access token
      fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      })
      .then(res => res.json())
      .then(userProfile => {

        // Almacenar los datos en localStorage
        localStorage.setItem('userEmail', userProfile.email);
        localStorage.setItem('userName', userProfile.name);
        localStorage.setItem('userPicture', userProfile.picture);

        // Realizar el POST al servidor de autenticación
        return fetch('https://qrguard-production.up.railway.app/api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            username: userProfile.name,
            email: userProfile.email
          })
        });
      })
      .then(res => {
        if (!res.ok) {
          throw new Error('Error en el inicio de sesión');
        }
        return res.json();
      })
      .then(data => {
        if (data.data && data.data.token) { // Verificar si hay un token en la respuesta
          // Almacenar el token en localStorage
          localStorage.setItem('token', data.data.token);

          // Redirigir a la página principal
          this.$router.push('/mainView');
        } else {
          throw new Error('Token no recibido');
        }
      })
      .catch(err => {
        console.error('Error durante el proceso de autenticación:', err);
      });
    },
    handleGoogleFailure(error) {
      console.error('Inicio de sesión con Google fallido:', error);
    }
  }
};
</script>

<style scoped>
@import url('../styles.css');

.landing {
  background-image: url('../assets/background-img.png');
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
  flex-direction: row;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  width: 100%;
  min-height: 550px;
  align-items: center;
  padding: 60px;
  gap: 20px;
}

.title {
  align-items: start;
  display: flex;
  flex-direction: column;
  font-family: var(--primary-font);
  font-size: 22px;
  font-weight: bold;
  color: white;
}

.title img {
  height: 170px;
  width: 150px;
}

.title button {
  background-color: white;
  font-family: var(--primary-font);
  font-size: 17px;
  border-radius: 10px;
  padding: 15px;
  color: var(--primary-color);
  margin-top: 20px;
}

.google {
  height: 40px;
  width: 250px;
  border: 1px solid #A9A9A9;
  border-radius: 10px;
  
}

.google:hover {
  cursor: pointer;
}

.cardL {
  background-color: white;
  border-radius: 20px;
  width: 350px;
  height: 270px;
  padding: 25px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 7px;
}

.cardL h4 {
  font-family: var(--primary-font);
  font-size: 23px;
  color: var(--title-color);
}

.cardL p {
  font-family: var(--primary-font);
  font-size: 15px;
  color: var(--title-color);
}

.info {
  background-color: #FAFAFA;
  font-family: var(--primary-font);
  font-size: 25px;
  color: var(--title-color);
  padding-top: 90px;
  padding-bottom: 130px;
}

.info h4 {
  font-size: 23px;
}

.benefits {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  margin-top: 30px;
  flex-wrap: wrap;
}

.cards {
  background-color: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  font-size: 24px;
  color: var(--primary-color);
  max-width: 350px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  margin-top: 20px;
}

.cards p {
  font-size: 17px;
  color: var(--title-color);
}

.cards img {
  height: 200px;
  width: 200px;
}

footer {
  background-color: #3B3838;
  color: white;
  height: 50px;
  font-size: 15px;
  display: flex;
  align-items: center;
  text-align: start;
  padding-left: 40px;
}

@media screen and (min-width: 769px) and (max-width: 1095px) {
  
  .title {
    align-items: center;
  }
}

@media screen and (max-width: 768px) {
  .landing{
    justify-content: center;
  }
  .title {
    align-items: center;
  }
}
</style>





