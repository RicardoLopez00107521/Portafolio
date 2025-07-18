<template>
  <div class="modal-container" v-if="visible">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">✕</button>
      <h3>Información del Permiso</h3>
      <p><strong>Fecha de Creación:</strong> {{ info.creationDate }}</p>
      <p><strong>Hora de Inicio:</strong> {{ info.beginHour }}</p>
      <p><strong>Hora de Fin:</strong> {{ info.endHour }}</p>
      <p><strong>Fechas:</strong></p>
      <ul>
        <li v-for="date in info.dates" :key="date">{{ date }}</li>
      </ul>
      <button @click="closeModal">Cerrar</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PermissionInfoModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    info: {
      type: Object,
      required: true
    }
  },
  methods: {
    closeModal() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
/* Estilos del modal */
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background: #ffffff;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
  position: relative;
}

.close-button {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  text-align: left;
}

.modal-content p, ul {
  margin-bottom: 20px;
  font-size: 14px;
  color: var(--title-color);
  font-family: var(--primary-font);
  text-align: left;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button:hover {
  background-color: #1d3a94;
}
</style>
