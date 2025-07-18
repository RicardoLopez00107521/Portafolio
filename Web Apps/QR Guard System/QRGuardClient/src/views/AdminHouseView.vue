<template>
  <div class="main-admin-house">
    <div class="main-card">
      <div class="header">
        <h2>Registro de hogares</h2>
        <button @click="showAddHouseModal = true">Agregar hogar</button>
      </div>
      <div class="table-container">
        <div class = "loader" v-if="loading">Cargando...</div>
        <div v-else>
          <table>
            <thead>
              <tr>
                <th>Nº de casa</th>
                <th>Encargado</th>
                <th>Habitantes</th>
                <th>Detalle</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="house in houses" :key="house.houseNumber">
                <td>{{ house.houseNumber }}</td>
                <td>{{ house.inChargeResident ? house.inChargeResident.userName : 'Sin residente asignado' }}</td>
                <td>{{ house.currentOccupancy }}</td>
                <td>
                  <router-link to="/mainView/adminHouseDetailView" class="admin-home">
                    <button class="admin-home-btn" @click="setHouseNumber(house.houseNumber)">Administrar</button>
                  </router-link>
                </td>
              </tr>
              <tr v-if="houses.length === 0">
                <td colspan="4">No se encontraron hogares.</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>
      </div>
    </div>

    <add-house-modal
      v-if="showAddHouseModal"
      @close="showAddHouseModal = false"
      @house-added="fetchHouses"
    />
  </div>
</template>

<script>
import AddHouseModal from '../components/AddHouseModal.vue'; // Importa el componente modal

export default {
  name: 'AdminHouseView',
  components: {
    AddHouseModal
  },
  data() {
    return {
      houses: [],
      loading: false,
      error: null,
      showAddHouseModal: false
    };
  },
  methods: {
    fetchHouses() {
      this.loading = true;
      this.error = null;
      const token = localStorage.getItem('token');
      fetch('https://qrguard-production.up.railway.app/api/home/get-all-homes', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => response.json())
      .then(data => {
        if (data.message === "OK") {
          this.houses = data.data;
        } else {
          this.error = 'Error al obtener los hogares.';
        }
      })
      .catch(error => {
        console.error('Error fetching houses:', error);
        this.error = 'Error al obtener los hogares.';
      })
      .finally(() => {
        this.loading = false;
      });
    },
    setHouseNumber(houseNumber) {
      localStorage.setItem('houseNumber', houseNumber);
    }
  },
  mounted() {
    this.fetchHouses();
  }
}
</script>

<style scoped>
@import url('../styles.css');

.main-admin-house {
  padding: 20px;
  margin-top: 20px;
}

.main-card {
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
}

.header button {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.header button:hover {
  background-color: #1d3a94;
}

.table-container {
  margin-top: 20px;
  overflow-x: auto;
}

.table-container table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  font-family: var(--primary-font);
}

.table-container thead {
  background-color: #f4f4f4;
}

.table-container th,
.table-container td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.table-container th {
  background-color: #f9f9f9;
  font-size: 14px;
}

.table-container td {
  font-size: 13px;
}

.admin-home-btn{
  background-color: var(--bg-color);
  color: var(--title-color);
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.admin-home-btn:hover {
  background-color: #E0E1E5;
}
.error-message {
  color: red;
  margin-top: 10px;
}

.loader {
  font-family: var(--primary-font);
}
</style>
