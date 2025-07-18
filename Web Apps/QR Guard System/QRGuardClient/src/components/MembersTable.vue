<template>
  <div class="table-container">
    <div class="table-header">
      <h3>Miembros del hogar</h3>
      <!-- Mostrar el botón de añadir solo si el usuario tiene los roles adecuados -->
      <button v-if="isAdminOrInCharge" class="add-button" @click="addMember">
        Añadir
      </button>
    </div>
    <div class="table-wrapper">
      <div class="loader" v-if="loading">Cargando...</div>
      <table v-else class="table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Correo electrónico</th>
            <!-- Mostrar la columna de acciones solo si el usuario tiene los roles adecuados -->
            <th v-if="isAdminOrInCharge">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <!-- Verificar si no hay miembros -->
          <tr v-if="members.length === 0">
            <td colspan="3">No hay miembros en este hogar</td>
          </tr>
          <!-- Mostrar los miembros si existen -->
          <tr v-for="(member, index) in members" :key="index">
            <td>{{ member.name }}</td>
            <td>{{ member.email }}</td>
            <!-- Mostrar el botón de eliminar solo si el usuario tiene los roles adecuados -->
            <td v-if="isAdminOrInCharge">
              <button class="delete-button" @click="showDeleteModal(member.email)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <DeleteMemberModal
      :visible="isDeleteModalVisible"
      :userEmail="userEmail"
      :houseNumber="houseNumber"
      @close="closeDeleteModal"
      @member-deleted="handleMemberDeleted"
    />
  </div>
</template>

<script>
import DeleteMemberModal from './DeleteMemberModal.vue';

export default {
  name: 'MembersTable',
  components: {
    DeleteMemberModal
  },
  props: {
    members: {
      type: Array,
      required: true
    },
    houseNumber: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      isDeleteModalVisible: false,
      userEmail: '',
      isAdminOrInCharge: false,  // Variable para verificar los roles
      loading: true // Estado de carga
    };
  },
  mounted() {
    this.checkUserRole();
  },
  methods: {
    showDeleteModal(email) {
      this.userEmail = email;
      this.isDeleteModalVisible = true;
    },
    closeDeleteModal() {
      this.isDeleteModalVisible = false;
      this.userEmail = '';
    },
    handleMemberDeleted() {
      this.$emit('member-deleted'); // Emitir evento hacia la vista principal
    },
    confirmDeleteMember() {
      this.$emit('remove-member', this.userEmail);
      this.closeDeleteModal();
    },
    addMember() {
      this.$emit('show-add-member-modal');
    },
    checkUserRole() {
      const url = 'https://qrguard-production.up.railway.app/api/auth/whoami';
      const token = localStorage.getItem('token');

      fetch(url, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Error al obtener roles de usuario');
        }
        return response.json();
      })
      .then(data => {
        const userRoles = data.data.userRoles;
        this.isAdminOrInCharge = userRoles.includes('ADMIN') || userRoles.includes('IN_CHARGE_RESIDENT');
      })
      .catch(error => {
        console.error('Error al obtener roles de usuario:', error.message);
        // Aquí podrías manejar el error de acuerdo a tus necesidades
      })
      .finally(() => {
        this.loading = false; // Ocultar el mensaje de carga después de obtener los roles
      });
    }
  }
};
</script>

<style scoped>
@import url('../styles.css');

.table-wrapper {
  overflow-x: auto;
}

.table-container {
  margin-top: 20px;
  background-color: var(--white-color);
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 20px;
  overflow-x: auto;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.add-button {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.add-button img {
  margin-right: 5px;
}

.add-button:hover {
  background-color: #1d3a94;
}

.table {
  width: 100%;
  border-collapse: collapse;
  color: var(--title-color);
  font-family: var(--primary-font);
}

.table th, .table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.table th {
  background-color: #f9f9f9;
  font-size: 14px;
}

.table td {
  background-color: var(--white-color);
  font-size: 13px;
}

.table-header h3 {
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
}

.delete-button {
  background-color: var(--bg-color);
  color: var(--title-color);
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #E0E1E5;
}

.loader {
  font-family: var(--primary-font);
}
</style>
