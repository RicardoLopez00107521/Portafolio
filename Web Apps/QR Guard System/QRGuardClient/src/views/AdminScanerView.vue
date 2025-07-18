<template>
  <div class="main-admin-scanner">
    <div class="add-scanner">
      <h2>Agregar un nuevo escáner</h2>
      <form @submit.prevent="addScanner">
        <label for="email">Correo del escáner</label>
        <input type="email" id="email" v-model="newScannerId" placeholder="Escáner" required />
        <label for="residentInCharge">Tipo de terminal</label>
        <select id="residentInCharge" v-model="terminalType">
          <option value="PEDESTRIAN_ACCESS">Peatonal</option>
          <option value="VEHICLE_ACCESS">Vehicular</option>
        </select>
        <button type="submit">Agregar escáner</button>
      </form>
    </div>
    <div class="active-scanner">
      <h2>Escáneres activos</h2>
      <div class="scanner-cards">
        <div class="scanner-card" v-for="scanner in scanners" :key="scanner.id">
          <CardScanner :src="scanner.src" :accessGate="scanner.accessGate" :id="scanner.id" @remove="removeScanner(scanner.id)" />
        </div>
        <p v-if="scanners.length === 0">No hay terminales activos.</p>
      </div>
    </div>
  </div>
</template>

<script>
import CardScanner from '../components/CardTablet.vue';
import scannerImage from '../assets/scaner.png';
import { useToast } from 'vue-toastification';

export default {
  name: 'AdminScannerView',
  components: {
    CardScanner
  },
  data() {
    return {
      newScannerId: '',
      terminalType: 'PEDESTRIAN_ACCESS',
      scanners: [], // Vacío al inicio, se llenará con los datos de la API
    };
  },
  created() {
    this.fetchScanners();
  },
  methods: {
    async fetchScanners() {
      try {
        const token = localStorage.getItem('token'); // Obtén el token desde localStorage
        const response = await fetch('https://qrguard-production.up.railway.app/api/admin/getTerminals', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        const data = await response.json();
        if (data.message === 'OK') {
          this.scanners = data.data.map(scanner => ({
            accessGate: scanner.accessGate,
            id: scanner.terminalId,
            src: scannerImage // Asigna la imagen predeterminada aquí
          }));
        } else {
          this.scanners = []; // Si no hay datos válidos, limpia la lista de escáneres
        }
      } catch (error) {
        console.error('Error al obtener los escáneres:', error);
        this.scanners = []; // En caso de error, limpia la lista de escáneres
        const toast = useToast();
        toast.error('Error al obtener los escáneres: ' + error.message);
      }
    },
    async addScanner() {
      try {
        const token = localStorage.getItem('token'); // Obtén el token desde localStorage
        const response = await fetch('https://qrguard-production.up.railway.app/api/admin/addTerminal', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            email: this.newScannerId,
            terminalAccess: this.terminalType
          })
        });

        const data = await response.json();
        if (response.ok) {
          // Recargar la lista de escáneres
          await this.fetchScanners();
          // Limpia el formulario
          this.newScannerId = '';
          this.terminalType = 'PEDESTRIAN_ACCESS';
          const toast = useToast();
          toast.success('Escáner agregado exitosamente');
        } else {
          console.error('Error al agregar el escáner:', data.message);
          const toast = useToast();
          toast.error('Error al agregar el escáner.');
        }
      } catch (error) {
        console.error('Error al agregar el escáner:', error);
        const toast = useToast();
        toast.error('Error al agregar el escáner: ' + error.message);
      }
    },
    async removeScanner(id) {
      try {
        const token = localStorage.getItem('token'); // Obtén el token desde localStorage
        const response = await fetch(`https://qrguard-production.up.railway.app/api/admin/deleteTerminal?terminalId=${id}`, {
          method: 'PATCH',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (!response.ok) {
          const data = await response.json();
          if (response.status === 404) {
            // No se encontró el terminal
            console.warn('No se encontró el terminal.');
          } else {
            throw new Error('Error al eliminar terminal');
          }
        } else {
          const data = await response.json();
        }
        // Después de intentar eliminar, siempre actualizamos la lista de terminales
        await this.fetchScanners();
      } catch (error) {
        console.error('Error al eliminar la terminal:', error.message);
      }
    }
  }
};
</script>



<style scoped>
@import url('../styles.css');

.main-admin-scanner {
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

.add-scanner {
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

.add-scanner h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 25px;
}

.add-scanner form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-scanner label {
  margin-bottom: 5px;
  text-align: left;
  font-weight: 600;
}

.add-scanner input,
.add-scanner select {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100%; /* Ajusta el ancho al 100% del contenedor */
}

.add-scanner input:focus,
.add-scanner select:focus {
  outline-color: var(--primary-color);
}

.add-scanner button {
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

.add-scanner button:hover {
  background-color: #1d3a94;
}

.active-scanner {
  margin-top: 20px;
  max-width: 100%;
}

.active-scanner h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 10px;
}

.scanner-cards {
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  gap: 20px;
}

.scanner-card {
  flex: 0 0 auto;
}
</style>

<style scoped>
@import url('../styles.css');

.main-admin-scanner {
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

.add-scanner {
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

.add-scanner h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 25px;
}

.add-scanner form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-scanner label {
  margin-bottom: 5px;
  text-align: left;
  font-weight: 600;
}

.add-scanner input,
.add-scanner select {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100%; /* Ajusta el ancho al 100% del contenedor */
}

.add-scanner input:focus,
.add-scanner select:focus {
  outline-color: var(--primary-color);
}

.add-scanner button {
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

.add-scanner button:hover {
  background-color: #1d3a94;
}

.active-scanner {
  margin-top: 20px;
  max-width: 100%;
}

.active-scanner h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 10px;
}

.scanner-cards {
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  gap: 20px;
}

.scanner-card {
  flex: 0 0 auto;
}

select {
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  padding: 10px; /* Asegura que el padding sea consistente */
  border: 1px solid #ddd; /* Mismo color de borde que el input */
  border-radius: 4px; /* Mismo radio de borde que el input */
}

select:focus {
  outline-color: var(--primary-color); /* Cambia el borde al color primario cuando está enfocado */
}
</style>


