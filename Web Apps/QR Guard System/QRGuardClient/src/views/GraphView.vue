<template>
  <div class="main-graph">
    <div class="cards-container">
      <div class="left-cards">
        <div class="card small-card">
          <h3>Entradas Totales</h3>
          <p>{{ totalEntries }}</p>
        </div>
        <div class="card small-card">
          <h3>Entradas últimos días</h3>
          <p>{{ lastDaysEntries }}</p>
        </div>
      </div>
      <div class="card big-card">
        <GraphicCard></GraphicCard>
      </div>
    </div>
  </div>
</template>

<script>
import GraphicCard from '../components/GraphicCard.vue';

export default {
  name: 'GraphView',
  components: {
    GraphicCard
  },
  data() {
    return {
      totalEntries: 0,
      lastDaysEntries: 0
    };
  },
  mounted() {
    this.fetchEntriesData();
  },
  methods: {
    fetchEntriesData() {
      const token = localStorage.getItem('token'); // Asegúrate de que el token está almacenado en localStorage
      fetch('https://qrguard-production.up.railway.app/api/entries/especial-format-entries', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      })
        .then(response => response.json())
        .then(data => {
          if (data.message === 'OK' && Array.isArray(data.data)) {
            this.totalEntries = data.data[0];
            this.lastDaysEntries = data.data[1];
          } else {
            console.error('Error en la respuesta del API:', data);
          }
        })
        .catch(error => {
          console.error('Error al realizar la petición:', error);
        });
    }
  }
}
</script>

<style scoped>
@import url('../styles.css');

.main-graph {
  padding: 20px;
  background-color: var(--bg-color); /* Fondo general de la vista */
}

.cards-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap; /* Para que los elementos puedan envolver en líneas nuevas */
}

.left-cards {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

.card {
  background-color: var(--white-color);
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  border: 1px solid transparent;
  display: flex;
  flex-direction: column; 
  align-items: center;
  justify-content: center;
  padding: 20px;
  margin-bottom: 20px;
  text-align: center;
  color: var(--title-color); 
  font-family: var(--primary-font); 
}

.small-card h3, .big-card h3 {
  font-size: var(--title-size); 
  color: var(--title-color); 
  font-size: 20px;
}

.small-card p, .big-card p {
  font-size: var(--title-size2); 
  color: var(--primary-color); 
  font-weight: 600;
  font-size: 40px;
}

.small-card {
  width: 250px;
  height: 250px;
}

.big-card {
  flex-grow: 1;
  height: 520px;
  margin-left: 20px;
}

@media (max-width: 860px) {
  .cards-container {
    flex-direction: column;
  }

  .big-card {
    margin-left: 0;
    margin-top: 20px;
  }

  .left-cards{
    align-items: center;
  }
}

</style>