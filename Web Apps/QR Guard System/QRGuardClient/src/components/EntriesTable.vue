<template>
  <div class="entries-table-container">
    <h3>Historial de entradas a mi hogar</h3>
    <div class="filter-container">
      <input type="text" placeholder="Buscar por nombre de visitante" v-model="searchQuery" />
      <div class="dropdown">
        <button @click="toggleDropdown" class="dropdown-button">
          Filtrar por fecha de entrada
        </button>
        <div v-if="dropdownVisible" class="dropdown-menu">
          <button @click="sortEntries('oldest')">Más antigua</button>
          <button @click="sortEntries('newest')">Más reciente</button>
        </div>
      </div>
    </div>
    <div class="table-wrapper">
      <div class = "loader" v-if="loading">Cargando...</div>
      <table v-else class="entries-table">
        <thead>
          <tr>
            <th>Fecha/Hora de entrada</th>
            <th>Visitante</th>
            <th>Correo electrónico</th>
            <th>Tipo de entrada</th>
          </tr>
        </thead>
        <tbody v-if="entries.length > 0">
          <tr v-for="entry in filteredEntries" :key="entry.id">
            <td>{{ entry.entryTimestamp }}</td>
            <td>{{ entry.enteredBy.userName }}</td>
            <td>{{ entry.enteredBy.userEmail }}</td>
            <td>{{ entry.entryType }}</td>
          </tr>
        </tbody>
        <tbody v-else>
          <tr>
            <td colspan="4">No hay Entradas a mi hogar</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EntriesTable',
  data() {
    return {
      searchQuery: '',
      dropdownVisible: false,
      entries: [],
      loading: true, // Estado de carga
    };
  },
  computed: {
    filteredEntries() {
      return this.entries.filter(entry =>
        entry.enteredBy.userName.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }
  },
  methods: {
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
    },
    sortEntries(order) {
      this.entries.sort((a, b) => {
        return order === 'newest' ? new Date(b.entryTimestamp) - new Date(a.entryTimestamp) : new Date(a.entryTimestamp) - new Date(b.entryTimestamp);
      });
      this.dropdownVisible = false;
    },
    async fetchEntries() {
      this.loading = true; // Mostrar el mensaje de carga
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/entries/get-my-house-entries', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          this.entries = data.data;
        } else if (response.status === 404) {
          this.entries = []; // Establece 'entries' como un array vacío cuando no hay entradas
        } else {
          console.error('Error al obtener las entradas:', response.statusText);
          // No mostrar alerta por 404 o mapeo
        }
      } catch (error) {
        console.error('Error al realizar la solicitud de entradas:', error);
        // No mostrar alerta por error de conexión o error general
      } finally {
        this.loading = false; // Ocultar el mensaje de carga
      }
    }
  },
  mounted() {
    this.fetchEntries();
  }
};
</script>

<style scoped>
@import url('../styles.css');

.table-wrapper {
  overflow-x: auto;
}

.entries-table-container {
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  overflow-x: auto;
}

.filter-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

input[type="text"] {
  width: 60%;
  padding: 10px;
  border: 1px solid #dcdcdc;
  border-radius: 5px;
}

.filter-container input{
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  border-color: var(--title-color);
}

.filter-container input:focus {
  outline-color: var(--primary-color);
}

.dropdown {
  position: relative;
}

.dropdown-button {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  padding: 10px 20px;
  font-weight: 600;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.dropdown-button:hover {
  background-color: #1d3a94;
}

.dropdown-menu {
  position: absolute;
  right: 0;
  background: white;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  overflow: hidden;
}

.dropdown-menu button {
  display: block;
  padding: 10px 20px;
  width: 100%;
  text-align: left;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  font-family: var(--primary-font);
  color: var(--title-color);
}

.dropdown-menu button:hover {
  background-color: #f0f0f0;
}

.entries-table {
  width: 100%;
  border-collapse: collapse;
  color: var(--title-color);
  font-family: var(--primary-font);
}

.entries-table th,
.entries-table td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.entries-table th {
  background-color: #f9f9f9;
  font-size: 14px;
}

.entries-table td {
  font-size: 13px;
}

.entries-table-container h3{
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  margin-bottom: 20px;
}

@media screen and (min-width: 769px) and (max-width: 900px) {
  .filter-container {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
}

@media screen and (max-width: 768px) {
  .filter-container {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
}

.loader {
  font-family: var(--primary-font);
}
</style>
