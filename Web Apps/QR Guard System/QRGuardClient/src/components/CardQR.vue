<template>
  <div class="card">
    <select v-model="selectedPermission" @change="checkSelection" class="dropdown">
      <option disabled value="">Selecciona la casa del permiso</option>
      <option v-for="house in houses" :key="house" :value="house">Casa #{{ house }}</option>
    </select>
    <div class="qr-placeholder" v-if="!qrCodeDataUrl">
      <img src="../assets/No-QR.png" alt="Aún no tienes un QR" class="placeholder-img"/>
    </div>
    <div v-else class="qr-code">
      <img :src="qrCodeDataUrl" alt="Código QR" class="qr-img"/>
    </div>
    <button @click="generateQRCode" :disabled="!selectedPermission" class="buttonQR">Generar QR</button>
  </div>
</template>

<script>
import QRCode from 'qrcode';

export default {
  name: 'CardQR',
  data() {
    return {
      qrCodeDataUrl: null,
      selectedPermission: '',
      houses: []
    };
  },
  methods: {
    async fetchPermits() {
      const token = localStorage.getItem('token');
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/permit/myPermits', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        const data = await response.json();
        if (data.message === 'OK') {
          const houses = [...new Set(data.data.map(permit => permit.houseNumber))];
          this.houses = houses;
        } else {
          console.error('Error fetching permits:', data.message);
        }
      } catch (error) {
        console.error('Error fetching permits:', error);
      }
    },
    async generateQRCode() {
      const currentDate = new Date();
      const currentTime = currentDate.toISOString();
      const userEmail = localStorage.getItem('userEmail');
      const QrInfo = {
        homeName: this.selectedPermission,
        userEmail: userEmail,
        qrTimestamp: currentTime
      };
      const content = JSON.stringify(QrInfo);

      try {
        this.qrCodeDataUrl = await QRCode.toDataURL(content);
      } catch (error) {
        console.error('Error generando el código QR:', error);
      }
    },
    checkSelection() {
      // Placeholder method if you want to perform additional actions on selection change
    }
  },
  mounted() {
    this.fetchPermits();
  }
}
</script>

<style scoped>
@import url('../styles.css');

.card {
  background-color: var(--white-color);
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1); 
  border-radius: 10px; 
  border: 1px solid transparent; 
  width: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  gap: 10px;
  text-align: center;
}

h2 {
  color: var(--title-color);
  font-size: var(--title-size2);
  font-family: var(--primary-font);
}

.qr-placeholder {
  width: 200px;
  height: 200px;
  position: relative;
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 5px;
}

.placeholder-img {
  width: 200px;
  height: 200px;
  border-radius: 5px;
}

.qr-code img {
  width: 100%;
  height: auto;
  border-radius: 5px;
}

.qr-code {
  width: 200px;
  height: 200px;
}

.dropdown {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-family: var(--primary-font);
}

.buttonQR {
  background-color: var(--primary-color);
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1); 
  border-radius: 5px;
  color: white;
  font-size: var(--menu-items-size);
  font-family: var(--primary-color);
  padding: 10px 0;
  width: 100%;
  font-weight: 600;
  cursor: pointer;
}

.buttonQR:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.buttonQR:hover:enabled {
  background-color: #1d3a94;
}
</style>