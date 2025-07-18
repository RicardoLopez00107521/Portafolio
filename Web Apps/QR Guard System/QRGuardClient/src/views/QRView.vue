<template>
  <div class="main-qr">
    <h1>QR Actual</h1>   
    <CardQR></CardQR>
  </div>
</template>

<script>
import CardQR from '@/components/CardQR.vue';

export default {
  name: 'QRView',
  components: {
    CardQR
  }
}
</script>

<style scoped>
@import url('../styles.css');

.main-qr {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  gap: 20px;
  background-color: var(--bg-color);
}

.main-qr h1 {
  color: var(--title-color);
  font-family: var(--primary-font);
  font-size: var(--title-size);
}
</style>
