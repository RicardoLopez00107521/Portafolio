<template>
  <div class="house-detail">
    <HomeInfoCard ref="homeInfoCard" :email="homeEmail" @resident-edited="handleResidentEdited" @capacity-edited="handleCapacityEdited" />
    <MembersTable 
      :members="members" 
      :houseNumber="houseNumber" 
      @remove-member="handleRemoveMember" 
      @show-add-member-modal="showAddMemberModal"
      @member-deleted="handleMemberDeleted"  
    />
    <AddMemberModal 
      :visible="isAddMemberModalVisible" 
      @close="hideAddMemberModal" 
      @add-member="handleAddMember" 
    />
    <DeleteMemberModal
      :visible="isDeleteModalVisible"
      :userEmail="emailToDelete"
      :houseNumber="houseNumber" 
      @close="closeDeleteMemberModal"
      @member-deleted="handleMemberDeleted" 
    />
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
import HomeInfoCard from '../components/CardHomeAdmin.vue';
import MembersTable from '../components/MembersTable.vue';
import AddMemberModal from '../components/AddMemberModal.vue';
import DeleteMemberModal from '../components/DeleteMemberModal.vue';
import EditHouseResidentModal from '../components/EditHouseResident.vue'; // Importar el modal de editar residente
import { useToast } from "vue-toastification";

export default {
  name: 'AdminHouseDetailView',
  components: {
    HomeInfoCard,
    MembersTable,
    AddMemberModal,
    DeleteMemberModal,
    EditHouseResidentModal // Agregar componente de modal de editar residente
  },
  data() {
    return {
      homeEmail: '',
      members: [],
      isAddMemberModalVisible: false,
      isDeleteModalVisible: false,
      showEditResidentModal: false,
      showEditCapacityModal: false,
      houseNumber: '',
      emailToDelete: ''
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
        this.homeEmail = data.data.inChargeResident ? data.data.inChargeResident.userEmail : 'Sin encargado';
        this.members = data.data.residents.map(resident => ({
          name: resident.userName,
          email: resident.userEmail,
          role: resident.userRoles.includes('IN_CHARGE_RESIDENT') ? 'Administrador' : 'Normal'
        }));
      } catch (error) {
        console.error('Error fetching home details:', error);
      }
    },
    handleRemoveMember(email) {
      this.emailToDelete = email;
      this.isDeleteModalVisible = true;
    },
    showAddMemberModal() {
      this.isAddMemberModalVisible = true;
    },
    hideAddMemberModal() {
      this.isAddMemberModalVisible = false;
    },
    async handleAddMember(email) {
      const toast = useToast();
      const token = localStorage.getItem('token');
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/home/link-new-resident', {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            houseNumber: this.houseNumber,
            userEmail: email
          })
        });
        
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const newMember = { name: 'Nuevo Miembro', email: email, role: 'Normal' };
        this.members.push(newMember);
        this.fetchHomeDetails();  // Actualiza la tabla de miembros
        this.hideAddMemberModal(); // Cierra el modal de añadir miembro
        toast.success('Miembro agregado exitosamente');
      } catch (error) {
        console.error('Error adding new member:', error);
        toast.error('Error al agregar miembro');
      }
    },
    closeDeleteMemberModal() {
      this.isDeleteModalVisible = false;
      this.emailToDelete = '';
    },
    handleMemberDeleted() {
      this.fetchHomeDetails();
    },
    handleResidentEdited() {
      // Actualiza datos después de editar el residente encargado
      this.fetchHomeDetails();
    },
    handleCapacityEdited() {
      // Actualiza datos después de editar la capacidad
      this.$refs.homeInfoCard.fetchHomeDetails();
    }
  }
};
</script>



<style scoped>
@import url('../styles.css');

.house-detail {
  padding: 20px;
}
</style>

