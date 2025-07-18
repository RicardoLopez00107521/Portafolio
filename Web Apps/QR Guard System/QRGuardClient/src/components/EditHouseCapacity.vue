<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h2>Modificar capacidad máxima</h2>
      <form @submit.prevent="confirmEdit">
        <div class="form-group">
          <label for="residentsAllowed">Nueva capacidad máxima:</label>
          <select id="residentsAllowed" v-model="residentsAllowed">
            <option v-for="n in 10" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <button type="submit">Confirmar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { useToast } from 'vue-toastification';

export default {
  name: 'EditHouseCapacityModal',
  props: {
    show: Boolean,
    houseData: Object
  },
  data() {
    return {
      residentsAllowed: this.houseData.residentsAllowed || '',
      residentInCharge: this.houseData.residentInCharge || '',
      qrValidity: this.houseData.qrValidity || '10 min',
      residents: ['John Doe', 'Carlos García', 'Ricardo Sibrian'] // Ejemplo de residentes
    };
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    async confirmEdit() {
      const toast = useToast();
      const updatedData = {
        houseNumber: localStorage.getItem('houseNumber'), 
        newHouseCapacity: this.residentsAllowed
      };
      
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/home/update-home-capacity', {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}` // Obtén el token desde localStorage
          },
          body: JSON.stringify(updatedData)
        });
        
        if (!response.ok) {
          throw new Error('Error en la actualización de la capacidad');
        }

        const result = await response.json();
        // Emitir el evento de confirmación con los datos actualizados
        this.$emit('capacity-edited', updatedData);
        // Mostrar un mensaje de éxito
        toast.success('Capacidad actualizada correctamente');
        // Cerrar el modal
        this.closeModal();
      } catch (error) {
        // Manejo de errores
        console.error(error);
        toast.error('Ocurrió un error al actualizar la capacidad');
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
