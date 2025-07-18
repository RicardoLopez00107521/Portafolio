<template>
  <div class="main-admin-vigilant">
    <div class="add-vigilant">
      <h2>Agregar un nuevo vigilante</h2>
      <form @submit.prevent="addVigilant">
        <label for="email">Correo electrónico del nuevo vigilante</label>
        <input type="email" id="email" v-model="newVigilantEmail" placeholder="Correo electrónico" required />
        <button type="submit">Agregar vigilante</button>
      </form>
    </div>
    <div class="active-vigilants">
      <h2>Vigilantes activos</h2>
      <div class="vigilant-cards">
        <div v-if="vigilants.length === 0">
          <p>No hay vigilantes activos.</p>
        </div>
        <div class="vigilant-card" v-for="vigilant in vigilants" :key="vigilant.vigilantId">
          <CardAdmin :src="vigilant.src" :name="vigilant.vigilantName" :email="vigilant.vigilantEmail" :id="vigilant.vigilantId" @remove="fetchVigilants" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CardAdmin from '../components/CardVigilante.vue';
import vigilantImage from '../assets/vigilant.png';
import { useToast } from 'vue-toastification';

export default {
  name: 'AdminVigilantView',
  components: {
    CardAdmin
  },
  data() {
    return {
      newVigilantEmail: '',
      vigilants: []
    };
  },
  mounted() {
    this.fetchVigilants();
  },
  methods: {
    fetchVigilants() {
      const url = 'https://qrguard-production.up.railway.app/api/admin/getGuards';
      const token = localStorage.getItem('token');

      fetch(url, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => {
        if (response.status === 404) {
          // No se encontraron vigilantes
          return { data: [] }; // Devuelve un objeto con datos vacíos para evitar 'undefined'
        }
        if (!response.ok) {
          throw new Error('Error al obtener vigilantes');
        }
        return response.json();
      })
      .then(data => {
        // Verifica si 'data.data' es undefined antes de mapearlo
        if (data.data) {
          this.vigilants = data.data.map(vigilant => ({
            vigilantId: vigilant.vigilantId,
            vigilantName: vigilant.vigilantName,
            vigilantEmail: vigilant.vigilantEmail,
            src: vigilantImage
          }));
        } else {
          this.vigilants = [];
        }
      })
      .catch(error => {
        // Si el error no es 404, muestra la alerta
        if (error.message !== 'Error al obtener vigilantes') {
          console.error('Error al obtener vigilantes:', error.message);
          const toast = useToast();
          toast.error('Error al obtener vigilantes: ' + error.message);
        }
      });
    },
    addVigilant() {
      if (this.newVigilantEmail) {
        const url = 'https://qrguard-production.up.railway.app/api/admin/addGuard';
        const token = localStorage.getItem('token');

        fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            guardEmail: this.newVigilantEmail
          })
        })
        .then(response => {
          console.log('API response status:', response.status);  // Agrega esto para ver el estado de la respuesta
          if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message); });
          }
          return response.json();
        })
        .then(data => {
          console.log('API response data:', data);  // Agrega esto para ver los datos de la respuesta
          const toast = useToast();
          toast.success('Vigilante agregado exitosamente');
          this.newVigilantEmail = ''; 
          this.fetchVigilants(); 
        })
        .catch(error => {
          console.error('Error al agregar vigilante:', error.message);
          const toast = useToast();
          toast.error('Error al agregar vigilante: ' + error.message);
        });
      }
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

.active-vigilants {
  margin-top: 20px;
  max-width: 100%;
}

.active-vigilants h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 10px;
}

.vigilant-cards {
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  gap: 20px;

}

.vigilant-card {
  flex: 0 0 auto;
}
</style>



