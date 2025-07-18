<template>
  <div class="modal-container" v-if="visible">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">✕</button>
      <h3>Eliminar Miembro</h3>
      <p>¿Estás seguro de que deseas eliminar este miembro?</p>
      <div class="modal-actions">
        <button type="button" @click="closeModal">Cancelar</button>
        <button type="submit" @click="confirmDelete">Eliminar</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DeleteMemberModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    userEmail: {
      type: String,
      required: true
    },
    houseNumber: {
      type: String,
      required: true
    }
  },
  methods: {
    async confirmDelete() {
      const token = localStorage.getItem('token');
      const houseNumber = this.houseNumber;
      const userEmail = this.userEmail;

      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/home/unbind-user', {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            houseNumber: houseNumber,
            userEmail: userEmail
          })
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        this.$emit('member-deleted');  // Emitir evento para actualizar la tabla en la vista principal
        this.closeModal();
      } catch (error) {
        console.error('Error deleting member:', error);
      }
    },
    closeModal() {
      this.$emit('close');
    }
  }
}
</script>









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


