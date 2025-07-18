<template>
  <div class="modal-container" v-if="visible">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">✕</button>
      <h3>Eliminar Terminal</h3>
      <p>¿Estás seguro de que deseas eliminar esta terminal?</p>
      <div class="modal-actions">
        <button type="button" @click="closeModal">Cancelar</button>
        <button type="submit" @click="confirmDelete">Eliminar</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DeleteTerminalModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    terminalId: {
      type: String,
      required: true
    }
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    async confirmDelete() {
      try {
        const url = `https://qrguard-production.up.railway.app/api/admin/deleteTerminal?terminalId=${this.terminalId}`;
        const token = localStorage.getItem('token');

        const response = await fetch(url, {
          method: 'PATCH',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (!response.ok) {
          throw new Error('Error al eliminar terminal');
        }

        const data = await response.json();
        console.log('API response data:', data);  // Log para depuración
        this.$emit('delete-terminal');
      } catch (error) {
        console.error('Error al eliminar la terminal:', error.message);
      } finally {
        this.closeModal();
      }
    }
  }
};
</script>



<style scoped>
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  text-align: center;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 18px;
}

.modal-actions {
  margin-top: 20px;
}
</style>


<style scoped>
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

.modal-content p {
  margin-bottom: 20px;
  font-size: 14px;
  color: var(--title-color);
  font-family: var(--primary-font);
  text-align: left;
}

.modal-actions {
  display: flex;
  justify-content: space-evenly;
  gap: 10px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button[type="button"] {
  background-color: var(--bg-color);
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button[type="button"]:hover {
  background-color: #E0E1E5;  
}

button[type="submit"] {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button[type="submit"]:hover {
  background-color: #1d3a94;
}
</style>

