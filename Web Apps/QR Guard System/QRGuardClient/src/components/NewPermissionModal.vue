<template>
  <div class="modal-container" v-if="visible">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">✕</button>
      <h3>Crear un nuevo permiso</h3>
      <form class="form-permission" @submit.prevent="confirmAddMember">
        <div class="form-group">
          <label for="email">Correo electrónico del visitante:</label>
          <input type="email" v-model="newMemberEmail" id="email" required />
        </div>

        <div class="form-group">
          <label for="residentInCharge">Tipo de entrada:</label>
          <select id="residentInCharge" v-model="entryType">
            <option value="UNIQUE">Entrada única</option>
            <option value="MULTIPLE">Entrada múltiple</option>
          </select>
        </div>

        <div class="form-group">
          <label for="qrValidity">Tipo de creación de permiso:</label>
          <select id="qrValidity" v-model="permissionType">
            <option value="ONE_DAY">Creación única</option>
            <option value="PERIODIC">Creación periódica</option>
          </select>
        </div>

        <div class="form-group" v-if="entryType === 'UNIQUE'">
          <label for="startTime">Hora de inicio:</label>
          <input type="time" v-model="startDateTime" id="startTime" required />
        </div>

        <div class="form-group" v-if="entryType === 'MULTIPLE'">
          <label for="startTime">Hora de inicio:</label>
          <input type="time" v-model="startDateTime" id="startTime" required />
          <label class="dd" for="endTime">Hora de fin:</label>
          <input type="time" v-model="endDateTime" id="endTime" required />
        </div>

        <div class="form-group" v-if="permissionType === 'ONE_DAY'">
          <label for="startDate">Fecha de inicio:</label>
          <input type="date" v-model="startDate" id="startDate" required />
        </div>

        <div class="form-group" v-if="permissionType === 'PERIODIC'">
          <label for="startDate">Fecha de inicio:</label>
          <input type="date" v-model="startDate" id="startDate" required />
          <label class="dd" for="endDate">Fecha de fin:</label>
          <input type="date" v-model="endDate" id="endDate" required />

          <div class="form-group, dd">
            <label>Días de la semana:</label>
            <div class="days-checkboxes">
              <label v-for="day in daysOfWeek" :key="day.value" class="days">
                <input type="checkbox" v-model="selectedDays" :value="day.value" />
                {{ day.label }}
              </label>
            </div>
          </div>
        </div>

        <div class="modal-actions">
          <button type="button" @click="closeModal">Cancelar</button>
          <button type="submit">Crear permiso</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { useToast } from 'vue-toastification';

export default {
  name: 'NewPermissionModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      newMemberEmail: '',
      startDateTime: '',
      endDateTime: '',
      startDate: '',
      endDate: '',
      entryType: '', // Se define en la opción seleccionada del dropdown
      permissionType: '', // Se define en la opción seleccionada del dropdown
      selectedDays: [],
      daysOfWeek: [
        { label: 'Lunes', value: 'MONDAY' },
        { label: 'Martes', value: 'TUESDAY' },
        { label: 'Miércoles', value: 'WEDNESDAY' },
        { label: 'Jueves', value: 'THURSDAY' },
        { label: 'Viernes', value: 'FRIDAY' },
        { label: 'Sábado', value: 'SATURDAY' },
        { label: 'Domingo', value: 'SUNDAY' },
      ],
      houseNumber: null, // Almacenará el número de casa obtenido desde la API
    };
  },
  methods: {
    closeModal() {
      this.clearForm();
      this.$emit('close');
    },
    async confirmAddMember() {
      // Obtener houseNumber desde la API
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/home/my-home-details', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        const data = await response.json();
        this.houseNumber = data.data.houseNumber;

      } catch (error) {
        console.error('Error al obtener los detalles de la casa:', error);
        const toast = useToast();
        toast.error('Hubo un error al obtener los detalles de la casa.');
        return;
      }

      // Formatear las horas para incluir segundos ":00"
      const formattedStartDateTime = this.startDateTime ? `${this.startDateTime}:00` : '';
      const formattedEndDateTime = this.endDateTime ? `${this.endDateTime}:00` : '';

      // Determinar qué datos enviar según las selecciones
      let requestData = {};
      if (this.entryType === 'UNIQUE' && this.permissionType === 'ONE_DAY') {
        requestData = {
          beginDate: this.startDate,
          beginTime: formattedStartDateTime,
          entryType: this.entryType,
          permitGenerationType: this.permissionType,
          guessEmail: this.newMemberEmail,
          targetHouse: this.houseNumber
        };
      } else if (this.entryType === 'UNIQUE' && this.permissionType === 'PERIODIC') {
        requestData = {
          beginDate: this.startDate,
          endDate: this.endDate,
          beginTime: formattedStartDateTime,
          days: this.selectedDays,
          entryType: this.entryType,
          permitGenerationType: this.permissionType,
          guessEmail: this.newMemberEmail,
          targetHouse: this.houseNumber
        };
      } else if (this.entryType === 'MULTIPLE' && this.permissionType === 'ONE_DAY') {
        requestData = {
          beginDate: this.startDate,
          beginTime: formattedStartDateTime,
          endTime: formattedEndDateTime,
          entryType: this.entryType,
          permitGenerationType: this.permissionType,
          guessEmail: this.newMemberEmail,
          targetHouse: this.houseNumber
        };
      } else if (this.entryType === 'MULTIPLE' && this.permissionType === 'PERIODIC') {
        requestData = {
          beginDate: this.startDate,
          endDate: this.endDate,
          beginTime: formattedStartDateTime,
          endTime: formattedEndDateTime,
          days: this.selectedDays,
          entryType: this.entryType,
          permitGenerationType: this.permissionType,
          guessEmail: this.newMemberEmail,
          targetHouse: this.houseNumber
        };
      } else {
        console.error('Combinación no válida de entrada y tipo de creación');
        const toast = useToast();
        toast.error('Combinación no válida de entrada y tipo de creación.');
        return;
      }

      // Realizar la llamada POST a la API
      try {
        const response = await fetch('https://qrguard-production.up.railway.app/api/permit/generate-permit', {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(requestData)
        });

        const responseData = await response.json();

        if (!response.ok) {
          const toast = useToast();
          toast.error(responseData.message || 'Hubo un error al crear el permiso.');
          return;
        }

        const toast = useToast();
        toast.success('Permiso creado exitosamente.');

        // Emitir evento hacia el componente padre (PermissionsTable.vue)
        this.$emit('permissionCreated');

        // Limpiar el formulario y cerrar el modal después de éxito
        this.clearForm();
        this.closeModal();
      } catch (error) {
        console.error('Error al hacer la solicitud POST:', error);
        const toast = useToast();
        toast.error('Hubo un error al crear el permiso.');
      }
    },
    clearForm() {
      this.newMemberEmail = '';
      this.entryType = '';
      this.permissionType = '';
      this.startDateTime = '';
      this.endDateTime = '';
      this.startDate = '';
      this.endDate = '';
      this.selectedDays = [];
    },
  }
}
</script>




<style scoped>
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 8888;
}

.dd{
  margin-top: 15px;
}

.modal-content {
  background: #ffffff;
  padding: 30px;
  border-radius: 10px;
  max-height: 500px;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
  position: relative;
}

.form-permission {
  overflow-y: auto;
  max-height: 400px;
  padding: 5px;
  flex-direction: column;
  display: flex;
  gap: 15px;
}

.close-button {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: var(--title-color);
  font-size: 18px;
  font-family: var(--primary-font);
  text-align: left;
}

label {
  display: block;
  margin-bottom: 10px; /* Ajuste el margen inferior aquí */
  font-size: 14px;
  color: var(--title-color);
  font-family: var(--primary-font);
  text-align: left;
}

input[type="email"], input[type="date"], input[type="time"], select {
  width: 100%;
  padding: 10px;
  border: 1px solid #dcdcdc;
  border-radius: 5px;
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  border-color: var(--title-color);
}

input:focus {
  outline-color: var(--primary-color);
}

.modal-actions {
  display: flex;
  justify-content: space-evenly;
  gap: 10px;
  margin-top: 15px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button[type="button"] {
  background-color: var(--bg-color);
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button[type="button"]:hover {
  background-color: #E0E1E5;  
}

button[type="submit"] {
  background-color: var(--primary-color);
  color: white;
  font-size: 14px;
  font-family: var(--primary-font);
  font-weight: 600;
}

button[type="submit"]:hover {
  background-color: #1d3a94;
}

select {
  color: var(--title-color);
  font-size: 14px;
  font-family: var(--primary-font);
  border-color: var (--title-color);
}

select:focus {
  outline-color: var(--primary-color);
}

.days-checkboxes {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.days-checkboxes label {
  display: flex;
  align-items: center;
  font-size: 14px;
  gap: 3px;
  color: var(--title-color);
  font-family: var(--primary-font);
}
</style>





