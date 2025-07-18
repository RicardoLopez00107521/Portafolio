<template>
  <div class="card-graph-container">
    <div class="card-graph">
      <h3>Registro de cantidad de entradas</h3>
      <div v-if="isDataLoaded">
        <Line :data="data" :options="options" />
      </div>
      <div v-else>
        <p>Cargando datos...</p>
      </div>
    </div>
  </div>
</template>

<script>
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement
} from 'chart.js'
import { Line } from 'vue-chartjs'

ChartJS.register(CategoryScale, LinearScale, LineElement, PointElement, Title, Tooltip, Legend)

export default {
  name: 'CardHome',
  components: {
    Line
  },
  data() {
    return {
      data: {
        labels: [],
        datasets: [
          {
            label: 'Entradas',
            backgroundColor: 'rgba(247, 121, 121, 0.5)',
            borderColor: '#f87979',
            fill: false,
            data: []
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            title: {
              display: true,
              text: 'Fecha'
            }
          },
          y: {
            title: {
              display: true,
              text: 'Cantidad de entradas'
            },
            beginAtZero: true,
            max: 30,
            display: true,
          }
        }
      },
      isDataLoaded: false // Bandera para controlar la visibilidad del gráfico
    }
  },
  async created() {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('https://qrguard-production.up.railway.app/api/entries/get-all-entries', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const jsonData = await response.json();
      const processedData = this.processData(jsonData.data);
      this.updateChartData(processedData);
      this.isDataLoaded = true; // Establecer la bandera a true después de que los datos hayan sido cargados y procesados
    } catch (error) {
      console.error('Error al obtener los datos:', error);
    }
  },
  methods: {
    processData(entries) {
      const entryCountByDate = entries.reduce((acc, entry) => {
        const date = new Date(entry.entryTimestamp).toLocaleDateString('es-ES', { day: '2-digit', month: 'short' });
        if (!acc[date]) {
          acc[date] = 0;
        }
        acc[date]++;
        return acc;
      }, {});

      const labels = Object.keys(entryCountByDate);
      const data = Object.values(entryCountByDate);

      return { labels, data };
    },
    updateChartData({ labels, data }) {
      this.data.labels = labels;
      this.data.datasets[0].data = data;
    }
  }
}
</script>

<style scoped>
@import url('../styles.css');

.card-graph-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh; /* Ajustado para que ocupe toda la altura de la ventana */
}

.card-graph {
  width: 80%; /* Ajustado para un ancho adecuado */
  height: 400px; /* Ajustado para acomodar el gráfico */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around; /* Modificado para distribuir el espacio de manera uniforme */
  padding: 20px;
}

.card-graph h3 {
  color: var(--title-color);
  font-size: 25px;
  font-family: var(--primary-font);
  margin: 0; /* Eliminado el margen inferior para un mejor control del espaciado */
  padding-top: 10px; /* Añadido un poco de espacio en la parte superior */
}

/* Asegurarse de que el gráfico ocupe todo el espacio disponible */
.vue-chartjs {
  width: 100%;
  height: 100%;
}
</style>