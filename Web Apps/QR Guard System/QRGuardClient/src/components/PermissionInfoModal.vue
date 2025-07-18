<template>
  <div v-if="visible" class="modal-overlay">
    <div class="modal-container">
      <h2>Información del permiso</h2>
      <p><strong>Fecha de creación:</strong> {{ details.creationDate }}</p>
      <p><strong>Hora de inicio:</strong> {{ details.beginHour }}</p>
      <p><strong>Hora de finalización:</strong> {{ details.endHour }}</p>
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
      required: true
    },
    details: {
      type: Object,
      default: () => ({ creationDate: '', beginHour: '', endHour: '' })
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
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-container {
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 500px;
}

h2 {
  margin-bottom: 20px;
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
}

p {
  margin-bottom: 20px;
  color: var(--text-color);
  font-size: 14px;
  font-family: var(--primary-font);
}

button {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #1d3a94;
}
</style>