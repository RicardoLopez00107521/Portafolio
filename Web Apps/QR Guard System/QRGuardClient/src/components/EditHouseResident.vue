<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h2>Modificar residente encargado</h2>
      <form @submit.prevent="confirmEdit">
        <div class="form-group">
          <label for="residentInCharge">Correo del nuevo residente encargado:</label>
          <input type="text" id="residentInCharge" v-model="residentInCharge" placeholder="Ingrese el residente encargado">
        </div>
        <div class="form-group">
          <button type="submit">Confirmar</button>
        </div>
      </form>
      <p class="note">Nota: Para designar un nuevo residente encargado, el mismo debe encontrarse registrado en el hogar.</p>
    </div>
  </div>
</template>

<script>
import { useToast } from 'vue-toastification';

export default {
  name: 'EditHouseResidentModal',
  props: {
    show: Boolean,
    houseData: Object
  },
  data() {
    return {
      residentInCharge: this.houseData.residentInCharge || ''
    };
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    async confirmEdit() {
      const toast = useToast();
      const dataToSend = {
        houseNumber: localStorage.getItem('houseNumber'),
        userEmail: this.residentInCharge
      };

      const token = localStorage.getItem('token');
      const url = `https://qrguard-production.up.railway.app/api/home/assign-in-charge-resident`;

      try {
        const response = await fetch(url, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(dataToSend)
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        // Emitir evento para indicar que se ha editado el residente encargado
        this.$emit('resident-edited');

        // Cerrar modal
        this.closeModal();

        // Mostrar toast de éxito
        toast.success('Se ha asignado correctamente el residente encargado.');

      } catch (error) {
        console.error('Error confirming edit:', error);
        // Mostrar toast de error
        toast.error('Hubo un error al asignar el residente encargado. Por favor, intenta nuevamente.');
      }
    }
  }
};
</script>


<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 8000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
}

h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-size: 14px;
  color: var(--title-color);
  font-family: var(--primary-font);
}


select, input {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

option{
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  border-color: var(--title-color);
}

option:focus {
  outline-color: var(--primary-color);
}

select{
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  border-color: var(--title-color);
}

input {
  font-family: var(--primary-font);
  border: 1.5px solid #ccc; 
  padding: 8px;
  outline: none; 
  font-size: 14px;
}

/* Cambia el borde al enfocar */
input:focus {
  border-color: var(--primary-color);
}

select:focus {
  outline-color: var(--primary-color);
}

button {
  background-color: var(--primary-color);
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
}

button:hover {
  background-color: #1d3a94;
}
.note {
  margin-top: 10px;
  font-size: 12px;
  color: var(--title-color);
  font-family: var(--primary-font);
}
</style>
