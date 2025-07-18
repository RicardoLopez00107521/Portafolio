<template>
  <div class="house-detail">
    <VisitantePermissionsTable
      :members="members" 
      @remove-member="handleRemoveMember" 
      @show-add-member-modal="showAddMemberModal" 
    />
    <NewPermissionModal
      :visible="isAddMemberModalVisible" 
      @close="hideAddMemberModal" 
      @add-member="handleAddMember" 
    />
  </div>
</template>

<script>
import HomeInfoCard from '../components/CardHome.vue';
import VisitantePermissionsTable from '../components/VisitantePermissionsTable.vue';
import NewPermissionModal from '../components/NewPermissionModal.vue';

export default {
  name: 'VisitantePermissionView',
  components: {
    HomeInfoCard,
    VisitantePermissionsTable,
    NewPermissionModal
  },
  data() {
    return {
      homeEmail: 'john.doe@gmail.com',
      members: [
        { visitante: 'ricardoo.sibrian@gmail.com', date: '24/03/2024', horaInicio: '15:30:00', horaFin: '17:30:00', tipo: 'Entrada única' },
      ],
      isAddMemberModalVisible: false
    };
  },
  methods: {
    handleRemoveMember(index) {
      this.members.splice(index, 1);
    },
    showAddMemberModal() {
      this.isAddMemberModalVisible = true;
    },
    hideAddMemberModal() {
      this.isAddMemberModalVisible = false;
    },
    handleAddMember(email) {
      const newMember = { visitante: 'visitante@gmail.com', date: '10/06/2024', horaInicio: '17:45:00', horaFin: '20:00:00', tipo: 'Entrada múltiple' };
      this.members.push(newMember);
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
