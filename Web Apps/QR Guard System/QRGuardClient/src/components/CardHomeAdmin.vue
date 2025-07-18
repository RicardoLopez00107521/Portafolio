<template>
  <div class="card-home">
    <div class="group-merge">
      <div class="group1">
        <img src="../assets/home.png" class="home-img"> 
        <div class="group2">
          <h4>Casa #{{ houseNumber }}</h4>
          <p>{{ inChargeResidentEmail }}</p>
        </div>
      </div>
      <div class="group3">
        <div class="group4">
          <h4>{{ currentOccupancy }}</h4>
          <p>Miembros actuales</p>
        </div>
        <div class="group5">
          <h4>{{ houseCapacity }}</h4>
          <div class="cap">
            <p>Miembros máximos</p>
            <button @click="openEditCapacityModal">
              <img src="../assets/edit_image.png" class="edit-cap" alt="Editar">
            </button>
          </div> 
        </div>
      </div>
    </div>
    <div class="group-merge2">
      <div class="group6">
        <p><strong>Residente encargado:</strong></p>
        <p class="email-resident">{{ inChargeResidentEmail }}</p>
        <button @click="openEditResidentModal">
          <img src="../assets/edit_image.png" class="edit-res" alt="Editar">
        </button>
      </div>
    </div>

    <!-- Modales -->
    <EditHouseResidentModal
      v-if="showEditResidentModal"
      :show="showEditResidentModal"
      :houseData="{ residentInCharge: inChargeResidentEmail }"
      @close="showEditResidentModal = false"
      @resident-edited="handleResidentEdited"
    />
    <EditHouseCapacityModal
      v-if="showEditCapacityModal"
      :show="showEditCapacityModal"
      :houseData="{ houseCapacity: houseCapacity }"
      @close="showEditCapacityModal = false"
      @capacity-edited="handleCapacityEdited"
    />
  </div>
</template>

<script>
import EditHouseResidentModal from './EditHouseResident.vue';
import EditHouseCapacityModal from './EditHouseCapacity.vue';

export default {
  name: 'HomeInfoCard',
  components: {
    EditHouseResidentModal,
    EditHouseCapacityModal,
  },
  data() {
    return {
      houseNumber: '',
      inChargeResidentEmail: '',
      houseCapacity: 0,
      currentOccupancy: 0,
      showEditResidentModal: false,
      showEditCapacityModal: false,
    };
  },
  created() {
    this.houseNumber = localStorage.getItem('houseNumber');
    this.fetchHomeDetails();
  },
  methods: {
    async fetchHomeDetails() {
      const token = localStorage.getItem('token');
      try {
        const response = await fetch(`https://qrguard-production.up.railway.app/api/home/my-home-details-by-number?number=${this.houseNumber}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();
        
        this.houseNumber = data.data.houseNumber;
        this.inChargeResidentEmail = data.data.inChargeResident ? data.data.inChargeResident.userEmail : "No asignado";
        this.houseCapacity = data.data.houseCapacity;
        this.currentOccupancy = data.data.currentOccupancy;
      } catch (error) {
        console.error('Error fetching home details:', error);
      }
    },
    openEditResidentModal() {
      this.showEditResidentModal = true;
    },
    openEditCapacityModal() {
      this.showEditCapacityModal = true;
    },
    handleResidentEdited() {
      // Llamar a fetchHomeDetails para actualizar los datos después de editar el residente
      this.fetchHomeDetails();
    },
    handleCapacityEdited() {
      // Llamar a fetchHomeDetails para actualizar los datos después de editar la capacidad
      this.fetchHomeDetails();
    }
  }
};
</script>



<style scoped>
@import url('../styles.css');
.card-home {
    background-color: var(--white-color);
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1); 
    border-radius: 10px; 
    border: 1px solid transparent; 
    width: 100%;
    height: auto;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-evenly;
    padding: 20px 0px 20px 0px;
    gap: 20px;
}

.max{
  display: flex;
}



.group-merge {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  width: 100%;
  align-items: center;
}

.group6 {
  display: flex;
  flex-direction: row;
  gap: 10px;
  color: var(--title-color);
  font-family: var(--primary-font);
  font-size: 15px;
  align-items: center; /* Alineación vertical */
}

.group6 .email-resident {
  background-color: var(--bg-color);
  border-radius: 5px;
  padding: 2px 10px 2px 10px;
}

.group1 {
  display: flex;
  flex-direction: row;
  gap: 15px;
  align-items: center;
}

.group1 img {
  width: 100px;
  height: 100px;
} 

.group2 {
  display: flex;
  flex-direction: column;
  color: var(--title-color);
  font-family: var(--primary-font);
  align-items: start;
}

.group2 h4 {
  font-size: var(--title-size);
}

.group2 p {
  font-size: 15px;
}

.group3 {
  display: flex;
  flex-direction: row;
  gap: 30px;
  color: var(--title-color);
  font-family: var(--primary-font);
}

.group4 {
  display: flex;
  flex-direction: column;
}

.group4 h4, .group5 h4 {
  font-size: var(--title-size);
}

.group4 p, .group5 p {
  font-size: 15px;
}

.group5 {
  display: flex;
  flex-direction: column;
}

.edit-cap {
  width: 20px !important;
  height: 20px !important;
  margin-left: 8px !important;
}

.edit-res {
  width: 20px !important;
  height: 20px !important;
  margin-right: 5px !important;
}

.edit-cap:hover {
  cursor:pointer;
}

.edit-res {
  cursor:pointer;
}

@media screen and (max-width: 768px) {
  .group-merge {
    flex-direction: column;
}

.group6{
  gap: 5px;
}

}
</style>
