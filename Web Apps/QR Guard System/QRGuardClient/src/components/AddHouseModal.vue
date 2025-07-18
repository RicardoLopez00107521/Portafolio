<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h2>Agregar hogar</h2>
      <form @submit.prevent="confirmAdd">
        <div class="form-group">
          <label for="houseNumber">Número de casa:</label>
          <input type="text" id="houseNumber" v-model="houseNumber" />
        </div>
        <div class="form-group">
          <label for="maxCapacity">Capacidad máxima:</label>
          <select id="maxCapacity" v-model="maxCapacity">
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
import { useToast } from "vue-toastification";

export default {
  name: 'AddHouseModal',
  data() {
    return {
      houseNumber: '',
      maxCapacity: 1
    };
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
    confirmAdd() {
      const toast = useToast();
      const token = localStorage.getItem('token');
      const newHouse = {
        houseNumber: this.houseNumber,
        houseCapacity: this.maxCapacity
      };
      fetch('https://qrguard-production.up.railway.app/api/home/create-home', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(newHouse)
      })
      .then(response => response.json())
      .then(data => {
        if (data.message === "Home created successfully. Remember to assign an in charge resident as soon as possible.") {
          toast.success('Hogar agregado correctamente');
          this.$emit('house-added'); // Emitir evento de que la casa fue agregada correctamente
          this.closeModal();
        } else {
          toast.error('Error al agregar el hogar');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        toast.error('Error al agregar el hogar');
      });
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
  
  input, select {
    width: 100%;
    padding: 10px;
    margin-top: 5px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  option {
    color: var(--title-color);
    font-size: 14px;
    font-family: var(--primary-font);
    border-color: var(--title-color);
  }
  
  option:focus {
    outline-color: var(--primary-color);
  }
  
  select {
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
  </style>