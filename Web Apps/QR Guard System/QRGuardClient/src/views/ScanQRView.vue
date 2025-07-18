<template>
  <div class="main-qr-scan">
    <h1>Coloca el código QR dentro del recuadro</h1>
    <div class="qr-scan" ref="qrScan">
      <qrcode-stream @decode="onDecode" @init="onInit"></qrcode-stream>
    </div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <!-- Sección para mostrar el contenido del QR debajo del escáner -->
    <div v-if="qrContent" class="qr-content">
      <p>Contenido del QR: {{ qrContent }}</p>
    </div>
  </div>
</template>

<script>
import { QrcodeStream } from 'vue3-qrcode-reader';

export default {
  name: 'ScanQRView',
  components: {
    QrcodeStream
  },
  data() {
    return {
      errorMessage: null,
      qrContent: null
    };
  },
  methods: {
    async onDecode(content) {
      console.log('QR Code Content:', content);
      this.qrContent = content;

      const token = localStorage.getItem('token');

      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/entries/request-entry', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: content
        });

        const data = await response.json();
        if (data.message !== 'OK') {
          this.errorMessage = 'Error al procesar la entrada: ' + data.message;
        } else {
          this.errorMessage = 'Entrada procesada correctamente';
        }
      } catch (error) {
        this.errorMessage = 'Estado de la peticion: ' + error.message;
      }
    },
    onInit(promise) {
      promise.catch(error => {
        if (error.name === 'NotAllowedError') {
          this.errorMessage = 'Acceso a la cámara denegado.';
        } else if (error.name === 'NotFoundError') {
          this.errorMessage = 'No se encontró ninguna cámara en el dispositivo.';
        } else {
          this.errorMessage = 'Error desconocido: ' + error.message;
        }
      });
    }
  }
}
</script>

<style scoped>
@import url('../styles.css');

.main-qr-scan {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  gap: 12px;
}

.main-qr-scan h1 {
  color: var(--title-color);
  font-family: var(--primary-font);
  font-size: var(--title-size);
}

.qr-scan {
  width: 350px;
  height: 350px;
  background-color: rgb(0, 0, 0);
  border-radius: 10px;
  overflow: hidden;
  position: relative;
}

.error-message {
  color: red;
  margin-top: 20px;
}

.qr-content {
  margin-top: 20px;
  color: var(--title-color);
}

</style>

