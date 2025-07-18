<template>
  <div class="permissions-table-container">
    <h2>Gestión de permisos</h2>
    <div class="tabs">
      <button :class="{ active: activeTab === 'solicitudes' && canViewSolicitudes }" @click="setActiveTab('solicitudes')" v-if="canViewSolicitudes">Historial de solicitudes</button>
      <button :class="{ active: activeTab === 'activos' }" @click="setActiveTab('activos')">Permisos activos</button>
    </div>
    <div class="filter-container">
      <input type="text" placeholder="Buscar por nombre de visitante" v-model="searchQuery" />
      <div class="dropdown">
        <button class="create-button" @click="showNewPermissionModal">Crear permiso</button>
      </div>
    </div>
    <div class="table-wrapper">
      <div class="loader "v-if="loading">Cargando...</div>
      <table v-else class="permissions-table">
        <thead>
          <tr>
            <th v-if="activeTab === 'solicitudes' && canViewSolicitudes">Solicitante</th>
            <th>Visitante</th>
            <th>Número de casa</th>
            <th>Tipo de permiso</th>
            <th>Detalle de permiso</th>
            <th v-if="activeTab === 'solicitudes' && canViewSolicitudes">Responder</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="permission in filteredPermissions" :key="permission.permitId">
            <td v-if="activeTab === 'solicitudes' && canViewSolicitudes">{{ permission.resident }}</td>
            <td>{{ permission.guess }}</td>
            <td>{{ permission.houseNumber }}</td>
            <td>{{ permission.entryType }}</td>
            <td><button class="admin-home" @click="showPermissionInfo(permission)">Información</button></td>
            <td v-if="activeTab === 'solicitudes' && canViewSolicitudes">
              <button class="resolve-button accept" @click="showAcceptModal(permission.permitId)">
                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#284AC7"><path d="M423.28-291.22 708.87-576.8l-62.46-62.7-223.13 223.13L312.15-527.5l-62.45 62.7 173.58 173.58ZM480-71.87q-84.91 0-159.34-32.12-74.44-32.12-129.5-87.17-55.05-55.06-87.17-129.5Q71.87-395.09 71.87-480t32.12-159.34q32.12-74.44 87.17-129.5 55.06-55.05 129.5-87.17 74.43-32.12 159.34-32.12t159.34 32.12q74.44 32.12 129.5 87.17 55.05 55.06 87.17 129.5 32.12 74.43 32.12 159.34t-32.12 159.34q-32.12 74.44-87.17 129.5-55.06 55.05-129.5 87.17Q564.91-71.87 480-71.87Zm0-91q133.04 0 225.09-92.04 92.04-92.05 92.04-225.09 0-133.04-92.04-225.09-92.05-92.04-225.09-92.04-133.04 0-225.09 92.04-92.04 92.05-92.04 225.09 0 133.04 92.04 225.09 92.05 92.04 225.09 92.04ZM480-480Z"/>
                </svg>
              </button>
              <button class="resolve-button deny" @click="showDeleteModal(permission.permitId)">
                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#535356"><path d="M338.63-280 480-421.37 621.37-280 680-338.63 538.63-480 680-621.37 621.37-680 480-538.63 338.63-680 280-621.37 421.37-480 280-338.63 338.63-280ZM480-71.87q-84.91 0-159.34-32.12-74.44-32.12-129.5-87.17-55.05-55.06-87.17-129.5Q71.87-395.09 71.87-480t32.12-159.34q32.12-74.44 87.17-129.5 55.06-55.05 129.5-87.17 74.43-32.12 159.34-32.12t159.34 32.12q74.44 32.12 129.5 87.17 55.05 55.06 87.17 129.5 32.12 74.43 32.12 159.34t-32.12 159.34q-32.12 74.44-87.17 129.5-55.06 55.05-129.5 87.17Q564.91-71.87 480-71.87Zm0-91q133.04 0 225.09-92.04 92.04-92.05 92.04-225.09 0-133.04-92.04-225.09-92.05-92.04-225.09-92.04-133.04 0-225.09 92.04-92.04 92.05-92.04 225.09 0 133.04 92.04 225.09 92.05 92.04 225.09 92.04ZM480-480Z"/>
                </svg>
              </button>
            </td>
          </tr>
          <tr v-if="filteredPermissions.length === 0 && permissions.length > 0">
            <td :colspan="activeTab === 'solicitudes' && canViewSolicitudes ? 6 : 5">No se encontraron permisos.</td>
          </tr>
          <tr v-if="permissions.length === 0">
            <td :colspan="activeTab === 'solicitudes' && canViewSolicitudes ? 6 : 5">No se encontraron permisos.</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- Modales -->
    <delete-request-modal :visible="isDeleteModalVisible" @close="isDeleteModalVisible = false" @confirm-delete="handleDelete" :permitId="permissionIdToDelete" />
    <accept-request-modal :visible="isAcceptModalVisible" @close="isAcceptModalVisible = false" @confirm-accept="handleAccept" :permitId="permissionIdToAccept" />
    <permission-info-modal :visible="isInfoModalVisible" :details="selectedPermissionInfo" @close="isInfoModalVisible = false" />
    <new-permission-modal :visible="isNewPermissionModalVisible" @close="isNewPermissionModalVisible = false" @permissionCreated="handlePermissionCreated" />
  </div>
</template>

<script>
import DeleteRequestModal from './DeleteRequestModal.vue';
import AcceptRequestModal from './AcceptRequestModal.vue';
import PermissionInfoModal from './PermissionInfoModal.vue';
import NewPermissionModal from '../components/NewPermissionModal.vue';

export default {
  name: 'PermissionsTable',
  components: {
    DeleteRequestModal,
    AcceptRequestModal,
    PermissionInfoModal,
    NewPermissionModal
  },
  data() {
    return {
      searchQuery: '',
      dropdownVisible: false,
      activeTab: 'activos',
      permissions: [],
      isDeleteModalVisible: false,
      permissionIdToDelete: null,
      isAcceptModalVisible: false,
      permissionIdToAccept: null,
      isInfoModalVisible: false,
      selectedPermissionInfo: {
        creationDate: '',
        beginHour: '',
        endHour: '',
        dates: []
      },
      isNewPermissionModalVisible: false,
      userRoles: [],
      loading: true // Estado de carga
    };
  },
  computed: {
    filteredPermissions() {
      return this.permissions.filter(permission =>
        permission.guess.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    canViewSolicitudes() {
      return this.userRoles.includes('IN_CHARGE_RESIDENT');
    },
    apiUrl() {
      return this.activeTab === 'solicitudes' ? 'https://qrguard-production.up.railway.app/api/permit/allPermits?state=PENDING' : 'https://qrguard-production.up.railway.app/api/permit/allPermits?state=APPROVED';
    }
  },
  methods: {
    async fetchPermissions() {
      try {
        const response = await fetch(this.apiUrl, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          this.permissions = data.data;
        } else if (response.status === 404) {
          this.permissions = [];
        } else {
          console.error('Error al obtener los permisos:', response.statusText);
        }
      } catch (error) {
        console.error('Error al realizar la solicitud de permisos:', error);
      } finally {
        this.loading = false; // Ocultar el mensaje de carga después de obtener los permisos
      }
    },
    async fetchUserRoles() {
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/auth/whoami', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          this.userRoles = data.data.userRoles;
          console.log('Roles del usuario:', this.userRoles);
          this.activeTab = this.canViewSolicitudes ? 'solicitudes' : 'activos';
          await this.fetchPermissions();
        } else {
          console.error('Error al obtener los roles del usuario:', response.statusText);
        }
      } catch (error) {
        console.error('Error al realizar la solicitud de roles del usuario:', error);
      }
    },
    setActiveTab(tab) {
      if (tab === 'solicitudes' && !this.canViewSolicitudes) {
        return;
      }
      this.activeTab = tab;
      this.loading = true; // Mostrar mensaje de carga mientras se obtienen los permisos
      this.fetchPermissions();
    },
    showNewPermissionModal() {
      this.isNewPermissionModalVisible = true;
    },
    handlePermissionCreated() {
      this.fetchPermissions();
    },
    async showPermissionInfo(permission) {
      try {
        const response = await fetch(`https://qrguard-production.up.railway.app/api/permit/permitById?permitId=${permission.permitId}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          this.selectedPermissionInfo = {
            creationDate: data.data.creationDate,
            dates: data.data.dates,
            beginHour: data.data.beginHour,
            endHour: data.data.endHour
          };
          this.isInfoModalVisible = true;
        } else {
          console.error('Error al obtener la información del permiso:', response.statusText);
        }
      } catch (error) {
        console.error('Error al realizar la solicitud de información del permiso:', error);
      }
    },
    showAcceptModal(permissionId) {
      this.permissionIdToAccept = permissionId;
      this.isAcceptModalVisible = true;
    },
    showDeleteModal(permissionId) {
      this.permissionIdToDelete = permissionId;
      this.isDeleteModalVisible = true;
    },
    async handleDelete() {
      try {
        this.loading = true; // Mostrar mensaje de carga mientras se obtienen los permisos
        await this.fetchPermissions();
      } catch (error) {
        console.error('Error al rechazar la solicitud:', error);
      } finally {
        this.loading = false; // Ocultar mensaje de carga después de obtener los permisos
      }
    },
    async handleAccept() {
      try {
        this.loading = true; // Mostrar mensaje de carga mientras se obtienen los permisos
        await this.fetchPermissions();
      } catch (error) {
        console.error('Error al aceptar la solicitud:', error);
      } finally {
        this.loading = false; // Ocultar mensaje de carga después de obtener los permisos
        this.isAcceptModalVisible = false;
      }
    }
  },
  mounted() {
    this.fetchUserRoles();
  }
};
</script>

<style scoped>
.table-wrapper {
  overflow-x: auto;
}

.permissions-table-container {
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-bottom: 20px;
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
}

.tabs {
  display: flex;
  margin-bottom: 20px;
}

.tabs button {
  flex: 1;
  padding: 10px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 14px;
  font-family: var(--primary-font);
  color: var(--title-color);
  border-bottom: 2px solid transparent;
}

.tabs button.active {
  border-bottom: 2px solid var(--primary-color);
  color: var(--primary-color);
  font-weight: 600;
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

.dropdown {
  position: relative;
}

.create-button {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.dropdown-button:hover {
  background-color: #1d3a94;
}

.permissions-table {
  width: 100%;
  border-collapse: collapse;
  color: var(--title-color);
  font-family: var(--primary-font);
}

.permissions-table th,
.permissions-table td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.permissions-table th {
  background-color: #f9f9f9;
  font-size: 14px;
}

.permissions-table td {
  font-size: 13px;
}

.resolve-button {
  border: none;
  background: none;
  cursor: pointer;
  font-size: 14px;
  padding-right: 12px; 
}

.admin-home {
  background-color: var(--bg-color);
  color: var(--title-color);
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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
