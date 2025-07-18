<template>
  <div class="main-admin-vigilant">
    <div class="add-vigilant">
      <h2>Registrar una entrada no asociada</h2>
      <form @submit.prevent="addEntry">
        <label for="entrada">¿Quién entra?</label>
        <input type="text" id="entrada" v-model="entryName" placeholder="Camión de aseo, policía, delivery..." required />
        <label for="motivo">Motivo de la entrada</label>
        <input type="text" id="motivo" v-model="entryMotive" placeholder="Motivo de entrada" required />
        <button type="submit">Registrar entrada</button>
      </form>
    </div>
  </div>
</template>

<script>
import { useToast } from 'vue-toastification';
import CardAdmin from '../components/CardVigilante.vue';
import vigilantImage from '../assets/vigilant.png';

export default {
  name: 'CreateEntryView',
  components: {
    CardAdmin
  },
  data() {
    return {
      entryName: '',
      entryMotive: '',
      vigilants: [
        { name: 'Carlos García', email: 'carlosgarcia@mail.com', src: vigilantImage },
      ]
    };
  },
  methods: {
    async addEntry() {
      if (!this.entryName || !this.entryMotive) {
        const toast = useToast();
        toast.error('Por favor, completa todos los campos.');
        return;
      }

      const token = localStorage.getItem('token');
      const url = 'https://qrguard-production.up.railway.app/api/guard/manualEntryRegister';
      const dataToSend = {
        name: this.entryName,
        motive: this.entryMotive
      };

      try {
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(dataToSend)
        });

        const responseData = await response.json();

        if (!response.ok) {
          const toast = useToast();
          toast.error(responseData.message || 'Hubo un error al registrar la entrada. Por favor, intenta nuevamente.');
          throw new Error('Network response was not ok');
        }

        // Limpiar los campos después de un registro exitoso
        this.entryName = '';
        this.entryMotive = '';

        // Mostrar alerta de éxito
        const toast = useToast();
        toast.success('Entrada registrada correctamente.');
      } catch (error) {
        console.error('Error registrando entrada:', error);
        const toast = useToast();
        toast.error('Hubo un error al registrar la entrada. Por favor, intenta nuevamente.');
      }
    },
    removeVigilant(email) {
      this.vigilants = this.vigilants.filter(v => v.email !== email);
    }
  }
};
</script>


<style scoped>
@import url('../styles.css');

.main-admin-vigilant {
  padding: 20px;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-color);
  font-family: var(--primary-font);
  align-items: center;
}

.header {
  text-align: center;
  margin-bottom: 20px;
}

.header h1 {
  font-size: var(--title-size);
  color: var(--title-color);
}

.add-vigilant {
  background-color: var(--white-color);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  width: 75%;
  
}

.add-vigilant h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 25px;
}

.add-vigilant form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-vigilant label {
  margin-bottom: 5px;
  text-align: left;
  font-weight: 600;
}

.add-vigilant input {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

input:focus {
  outline-color: var(--primary-color);
}

.add-vigilant button {
  background-color: var(--primary-color);
  font-size: 14px;
  color: white;
  font-family: var(--primary-font);
  font-weight: 600;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-vigilant button:hover {
  background-color: #1d3a94;
}

</style>