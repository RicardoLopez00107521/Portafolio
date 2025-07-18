<template>
  <div class="house-detail">
    <HomeInfoCard ref="homeInfoCard" :email="homeEmail || 'No asignado'" />
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
  </div>
</template>

<script>
import { useToast } from "vue-toastification";
import HomeInfoCard from '../components/CardHome.vue';
import MembersTable from '../components/MembersTable.vue';
import AddMemberModal from '../components/AddMemberModal.vue';
import DeleteMemberModal from '../components/DeleteMemberModal.vue';

export default {
  name: 'AdminHouseDetailView',
  components: {
    HomeInfoCard,
    MembersTable,
    AddMemberModal,
    DeleteMemberModal
  },
  data() {
    return {
      homeEmail: '',
      members: [],
      isAddMemberModalVisible: false,
      isDeleteModalVisible: false,
      houseNumber: '',
      emailToDelete: ''
    };
  },
  created() {
    this.fetchHomeDetails();
  },
  methods: {
    async fetchHomeDetails() {
      const token = localStorage.getItem('token');
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/home/my-home-details', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();

        this.houseNumber = data.data.houseNumber;
        this.homeEmail = data.data.inChargeResident ? data.data.inChargeResident.userEmail : 'Sin asignar';
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
        this.handleMemberAdded();
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
      this.$refs.homeInfoCard.fetchHomeDetails();
    },
    handleMemberAdded() {
      this.fetchHomeDetails();
      this.$refs.homeInfoCard.fetchHomeDetails();
    }
  }
}
</script>


<style scoped>
@import url('../styles.css');

.house-detail {
  padding: 20px;
}
</style>








